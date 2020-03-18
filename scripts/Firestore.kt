import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient

fun configureFirebase(): Firestore {
    val options = FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .setProjectId("fir-semi-live-test")
        .build()

    FirebaseApp.initializeApp(options)

    return FirestoreClient.getFirestore()
}
