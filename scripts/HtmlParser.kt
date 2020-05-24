import it.skrape.core.htmlDocument
import it.skrape.extract
import it.skrape.selects.Doc
import it.skrape.skrape
import java.net.URL

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

fun Doc.parseWeeklyItems() = findAll(".issue>div>*") {
    var currentHeader = ""
    mapNotNull { element ->
        when {
            "> h2" in element.cssSelector -> {
                currentHeader = element.text
                null
            }
            "> p" in element.cssSelector -> {
                val header = element.select("a").firstOrNull()?.text
                val description = element.html.substringAfterLast("<br>").trim()
                val link = element.findFirst("a").attribute("href")
                val location = element.select("span")
                    .firstOrNull()
                    ?.text
                    ?.removeSurrounding("(", ")")
                    ?: link.parseHost()
                WeeklyItem(header ?: link, description, currentHeader, link, location)
            }
            else -> {
                if (element.text.trim().isEmpty()) {
                    return@mapNotNull null
                }
                val header = element.findFirst("a:nth-child(2)").text.trim()
                val description = element.findFirst("p").text.trim()
                val link = element.findFirst("a").attribute("href")
                val location = element.findFirst("span").text.removeSurrounding("(", ")")
                val img = element.findFirst("a>img").attribute("src")
                WeeklyItem(header, description, currentHeader, link, location, img)
            }
        }
    }
}

fun String.parseHost() = URL(this).host.removePrefix("www.")

