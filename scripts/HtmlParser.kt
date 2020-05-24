import it.skrape.core.htmlDocument
import it.skrape.extract
import it.skrape.selects.Doc
import it.skrape.skrape

fun parse(issueNumber: Int): AndroidWeeklyIssue? = skrape {
    url = "https://androidweekly.net/issues/issue-$issueNumber"

    extract {
        htmlDocument {

            findFirstOrNull(".issues h2:first-of-type") ?: return@htmlDocument null

            AndroidWeeklyIssue(
                issueNumber,
                date = parseDate(),
                items = parseWeeklyItems()
            )
        }
    }
}

fun Doc.parseDate() = findFirst(".issues small").text.also(::println)

fun Doc.parseWeeklyItems() = findFirst(".issue>div") {
    findAll("h2,p") {
        var currentHeader = ""
        mapNotNull { element ->
            if ("h2" in element.cssSelector) {
                currentHeader = element.text
                null
            } else {
                val link = element.findFirst("a").attribute("href")
                WeeklyItem(link, element.text.dropWhile { it != ' ' }.trim(), currentHeader)
            }
        }
    }
}
