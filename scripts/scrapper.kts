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

val db = configureFirebase(projectId)

val issues = mutableListOf<AndroidWeeklyIssue>()

val notFound = listOf(303, 344, 363, 365)
for (issueNumber in 256..Int.MAX_VALUE) {
    if (issueNumber in notFound) {
        continue
    }

    println("Parsing $issueNumber")
    val issue = parse(issueNumber) ?: break
    println("Parsed issue #${issue.number} of ${issue.date} (Items: ${issue.items.count()})")
    issues += issue
}

println("${issues.count()} Issues Found!")

db.storeIssues(issues)

println("Issues saved")
