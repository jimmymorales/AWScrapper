#!/usr/bin/env kscript

@file:DependsOnMaven("it.skrape:skrapeit-core:1.0.0-alpha6")

@file:Include("WeeklyItem.kt")

@file:CompilerOpts("-jvm-target 1.8")

import it.skrape.core.htmlDocument
import it.skrape.extract
import it.skrape.selects.html5.span
import it.skrape.selects.html5.strong
import it.skrape.selects.html5.table
import it.skrape.skrape

val androidWeeklyIssue = skrape {
    url = "https://mailchi.mp/androidweekly/android-weekly-256"

    extract {
        htmlDocument {
            val issueNumber = strong(".issue-number") { findFirst { text.removePrefix("#").toInt() } }
            val date = span(".date") { findFirst { text } }

            val weeklyItems = table {
                rawCssSelector = "#archivebody > center > div > table"
                findAll {
                    var currentHeader = ""
                    mapNotNull { element ->
                        val header = element.findAll("h2").firstOrNull()?.text
                        if (header != null) {
                            currentHeader = header
                            null
                        } else {
                            val headlineElement = element.findFirst("a.article-headline")
                            val headline = headlineElement.text
                            val link = headlineElement.attribute("href")
                            val mainUrl = element.findFirst("span.main-url")
                                .text.removeSurrounding(prefix = "(", suffix = ")")
                            val description = element.findFirst("p").text
                            val imgLink = element.findAll("img").firstOrNull()?.attribute("src")
                            WeeklyItem.Article(headline, link, mainUrl, description, imgLink)
                        }
                    }
                }
            }

            AndroidWeeklyIssue(
                issueNumber,
                date,
                weeklyItems
            )
        }
    }
}

println(androidWeeklyIssue)

data class AndroidWeeklyIssue(
    val number: Int,
    val date: String,
    val items: List<WeeklyItem>
)

