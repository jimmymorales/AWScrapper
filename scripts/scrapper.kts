#!/usr/bin/env kscript

@file:DependsOn("it.skrape:skrapeit-core:1.0.0-alpha6")

@file:Include("WeeklyItem.kt")
@file:Include("AndroidWeeklyIssue.kt")

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
                            when (currentHeader.toUpperCase()) {
                                "ARTICLES & TUTORIALS" ->
                                    WeeklyItem.Article(headline, link, mainUrl, description, imgLink)
                                "SPONSORED" -> WeeklyItem.Sponsored(headline, link, mainUrl, description, imgLink)
                                "LIBRARIES & CODE" ->
                                    WeeklyItem.Library(headline, link, mainUrl, description, imgLink)
                                "VIDEOS & PODCASTS" ->
                                    WeeklyItem.Video(headline, link, mainUrl, description, imgLink)
                                else -> WeeklyItem.Unknown(headline, link, mainUrl, description, imgLink)
                            }
                        }
                    }
                }
            }

            AndroidWeeklyIssue(issueNumber, date, weeklyItems)
        }
    }
}

androidWeeklyIssue.items.forEach(::println)

