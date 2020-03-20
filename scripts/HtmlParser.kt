import it.skrape.core.htmlDocument
import it.skrape.extract
import it.skrape.selects.Doc
import it.skrape.selects.html5.div
import it.skrape.selects.html5.table
import it.skrape.skrape

fun parse(issueNumber: Int): AndroidWeeklyIssue? = skrape {
    url = "https://mailchi.mp/androidweekly/android-weekly-${getIssueNumberPath(issueNumber)}"

    extract {
        htmlDocument {
            val number = parseIssueNumber() ?: return@htmlDocument null

            AndroidWeeklyIssue(
                number,
                date = parseDate(),
                items = parseWeeklyItems()
            )
        }
    }
}

fun Doc.parseIssueNumber() = findFirstOrNull(cssSelector = "strong.issue-number")?.text?.removePrefix("#")?.toInt()

fun getIssueNumberPath(issueNumber: Int): String = when (issueNumber) {
    404 -> "404-its-just-the-issue-number-not-a-problem"
    371 -> "370-me929mv31o"
    else -> issueNumber.toString()
}


fun Doc.parseDate(): String {
    val date = findFirstOrNull("span.date")?.text
    return date ?: div {
        rawCssSelector = "#archivebody > center > table:nth-child(2) > tbody > tr:nth-child(2) > td > div"
        findFirst { text }
    }
}

fun Doc.parseWeeklyItems() = table {
    rawCssSelector = "#archivebody > center > div > table"
    findAll {
        var currentHeader = ""
        var isSponsoredItem = false
        mapNotNull { element ->
            val header = element.findAll("h2").firstOrNull()?.text
            if (header != null) {
                currentHeader = header
                null
            } else {
                if (element.findAll("h5").any()) {
                    isSponsoredItem = true
                    return@mapNotNull null
                }

                val headlineElement = element.findAll("a.article-headline").firstOrNull() ?: return@mapNotNull null
                val headline = headlineElement.text
                val link = headlineElement.attribute("href")
                val subHeadline = element.findFirst("span.main-url")
                    .text.removeSurrounding(prefix = "(", suffix = ")")
                val description = element.findFirst("p").text
                val imgLink = element.findAll("img").firstOrNull()?.attribute("src")

                if (isSponsoredItem) {
                    isSponsoredItem = false
                    return@mapNotNull WeeklyItem.Sponsored(headline, link, description, subHeadline, imgLink)
                }

                when (currentHeader.toUpperCase()) {
                    "ARTICLES & TUTORIALS" ->
                        WeeklyItem.Article(headline, link, description, subHeadline, imgLink)
                    "SPONSORED" ->
                        WeeklyItem.Sponsored(headline, link, description, subHeadline, imgLink)
                    "LIBRARIES & CODE" ->
                        WeeklyItem.Library(headline, link, description, subHeadline, imgLink)
                    "VIDEOS & PODCASTS" ->
                        WeeklyItem.Video(headline, link, description, subHeadline, imgLink)
                    "JOBS" ->
                        WeeklyItem.Job(headline, link, description, subHeadline)
                    "NEWS" ->
                        WeeklyItem.News(headline, link, description, subHeadline, imgLink)
                    "SPECIALS" ->
                        WeeklyItem.Special(headline, link, description, subHeadline, imgLink)
                    "DESIGN" ->
                        WeeklyItem.Design(headline, link, description, subHeadline, imgLink)
                    "EVENTS" ->
                        WeeklyItem.Event(headline, link, description, subHeadline, imgLink)
                    "TOOLS" ->
                        WeeklyItem.Event(headline, link, description, subHeadline, imgLink)
                    "BUSINESS" ->
                        WeeklyItem.Event(headline, link, description, subHeadline, imgLink)
                    else ->
                        WeeklyItem.Unknown(headline, link, description, subHeadline, imgLink)
                }
            }
        }
    }
}
