#!/usr/bin/env kscript

@file:DependsOn("com.google.firebase:firebase-admin:6.14.0")
@file:DependsOn("it.skrape:skrapeit-core:1.0.0-alpha6")

@file:Include("helpers/AndroidWeeklyIssue.kt")
@file:Include("helpers/Firestore.kt")
@file:Include("helpers/HtmlParser.kt")

@file:CompilerOpts("-jvm-target 1.8")

import kotlin.system.exitProcess

val projectId: String? = System.getenv("FIREBASE_PROJECT_ID")

if (projectId.isNullOrBlank()) {
    println("Missing project id (FIREBASE_PROJECT_ID) environment variable")
    exitProcess(-1)
}

val db = configureFirebase(projectId!!)

val issues = mutableListOf<AndroidWeeklyIssueItem>()

for (issueNumber in 291..Int.MAX_VALUE) {
    println("Parsing $issueNumber")
    val items = parse(issueNumber) ?: break
    println("Parsed issue #${items[0].issueNumber} of ${items[0].issueDate} (Items: ${items.count()})")
    issues += items
}

println("${issues.count()} Issues Found!")

db.storeIssues(issues)

println("Issues saved")
