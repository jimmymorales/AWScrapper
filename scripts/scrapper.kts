#!/bin/bash

//usr/bin/env echo '
/**** BOOTSTRAP kscript ****\'>/dev/null
command -v kscript >/dev/null 2>&1 || curl -L "https://git.io/fpF1K" | bash 1>&2
exec kscript $0 "$@"
\*** IMPORTANT: Any code including imports and annotations must come after this line ***/


@file:DependsOn("com.google.firebase:firebase-admin:6.12.2")
@file:DependsOn("it.skrape:skrapeit-core:1.0.0-alpha6")

@file:Include("AndroidWeeklyIssue.kt")
@file:Include("Firestore.kt")
@file:Include("HtmlParser.kt")

@file:CompilerOpts("-jvm-target 1.8")

/*val projectId = System.getenv("FIREBASE_PROJECT_ID")

if (projectId.isNullOrBlank()) {
    println("Missing project id (FIREBASE_PROJECT_ID) environment variable")
    exitProcess(-1)
}

val db = configureFirebase(projectId)*/

val issues = mutableListOf<AndroidWeeklyIssue>()

for (issueNumber in 1..Int.MAX_VALUE) {
    println("Parsing $issueNumber")
    val issue = parse(issueNumber) ?: break
    println(issue)
    //println("Parsed issue #${issue.number} of ${issue.date} (Items: ${issue.items.count()})")
    issues += issue
}

println("${issues.count()} Issues Found!")

//db.storeIssues(issues)

println("Issues saved")
