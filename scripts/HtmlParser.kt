import it.skrape.core.htmlDocument
import it.skrape.extract
import it.skrape.selects.Doc
import it.skrape.skrape

fun parse(issueNumber: Int): List<AndroidWeeklyIssueItem>? = skrape {
    url = "https://androidweekly.net/issues/issue-$issueNumber"

    extract {
        htmlDocument {
            if (parseIssueNumber() == null) {
                return@htmlDocument null
            }

            parseWeeklyItems(issueNumber, issueDate = parseDate())
        }
    }
}

fun Doc.parseIssueNumber() = findFirstOrNull(cssSelector = ".issues>div>div>h2")?.text

fun Doc.parseDate(): String = findFirst(".issues>div>div>small").text

fun Doc.parseWeeklyItems(issueNumber: Int, issueDate: String) = findAll("div.issue>table") {
    var currentHeader = ""
    var isSponsoredItem = false
    mapNotNull { element ->
        val header = element.findAll("tbody>tr:nth-child(1)>td>h2").firstOrNull()
        if (header != null) {
            currentHeader = header.text.trim()
            return@mapNotNull null
        }

        if (element.findAll("h5").any()) {
            isSponsoredItem = true
            return@mapNotNull null
        }

        val headlineElement = element.findFirst("a.article-headline")
        val headline = headlineElement.text.trim()
        val link = headlineElement.attribute("href")
        val mainUrl = element.findFirst("span.main-url").text
                .removeSurrounding(prefix = "(", suffix = ")")
                .trim()
        val description = element.findFirst("p").text.trim()
        val imgLink = element.findAll("a>img").firstOrNull()?.attribute("src")

        AndroidWeeklyIssueItem(
                issueNumber,
                issueDate,
                headline,
                link,
                description,
                mainUrl,
                currentHeader,
                imgLink,
                isSponsoredItem
        ).also { isSponsoredItem = false }
    }
}
