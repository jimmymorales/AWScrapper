#!/usr/bin/env kscript

@file:DependsOn("com.google.firebase:firebase-admin:6.12.2")
@file:DependsOn("it.skrape:skrapeit-core:1.0.0-alpha6")

@file:Include("AndroidWeeklyIssue.kt")
@file:Include("Firestore.kt")
@file:Include("HtmlParser.kt")
@file:Include("WeeklyItem.kt")

@file:CompilerOpts("-jvm-target 1.8")

import kotlin.system.exitProcess

val projectId = System.getenv("FIREBASE_PROJECT_ID")

if (projectId.isNullOrBlank()) {
    println("Missing project id (FIREBASE_PROJECT_ID) environment variable")
    exitProcess(-1)
}

configureFirebase(projectId)

val issues = mutableListOf<AndroidWeeklyIssue>()

val notFound = listOf(303, 344, 363, 365)
for (issueNumber in 256..Int.MAX_VALUE) {
    if (issueNumber in notFound) {
        continue
    }

    println("Parsing $issueNumber")
    val androidWeeklyIssue = parse(issueNumber) ?: break
    println("Parsed issue number = ${androidWeeklyIssue.number} on ${androidWeeklyIssue.date} (Items: ${androidWeeklyIssue.items.count { it is WeeklyItem.Unknown }})")
    issues += androidWeeklyIssue
}

println("${issues.count()} Issues Found!")
println("${issues.map { it.items.count() }.sum()} Items Found!")
