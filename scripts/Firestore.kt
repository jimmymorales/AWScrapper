import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient

fun configureFirebase(projectId: String): Firestore {
    val options = FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .setProjectId(projectId)
        .build()

    FirebaseApp.initializeApp(options)

    return FirestoreClient.getFirestore()
}

fun Firestore.storeIssues(issues: List<AndroidWeeklyIssue>) {
    issues.forEach { issue ->
        val issueNumber = issue.number.toString()
        collection("issues").document(issueNumber).set(mapOf("date" to issue.date)).get()

        issue.items.forEach { item ->
            collection("issues")
                .document(issueNumber)
                .collection("items")
                .document()
                .set(item.toFirestoreMap())
                .get()
        }
    }
}

fun WeeklyItem.toFirestoreMap() = mapOf(
    "headline" to headline,
    "link" to link,
    "description" to description,
    "mainUrl" to mainUrl,
    "imageLink" to imgLink,
    "type" to type.name
)
