package za.co.goodstuffcatering.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import za.co.goodstuffcatering.MainActivity
import za.co.goodstuffcatering.R

/**
 * This service runs in the background and listens for messages sent from Firebase.
 */
class PushNotificationService : FirebaseMessagingService() {

    /**
     * Triggered when a push notification is received while the app is in the foreground.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Extract the title and body from the notification payload
        val title = remoteMessage.notification?.title ?: "Good Stuff Catering"
        val body = remoteMessage.notification?.body ?: "You have an update on your booking."

        // Build and display the notification
        sendNotification(title, body)
    }

    private fun sendNotification(title: String, body: String) {
        // Create an Intent to open MainActivity when the user taps the notification
        val intent = Intent(this, MainActivity::class.java).apply {
            // Clears the backstack so it opens fresh
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        // Wrap the intent in a PendingIntent.
        // FLAG_IMMUTABLE is required by Android 12+ for security reasons.
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        // Define a channel ID. Channels group notifications together on Android 8.0+
        val channelId = "catering_updates"

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // The app icon
            .setContentTitle(title)             // The title of the notification
            .setContentText(body)               // The message body
            .setAutoCancel(true)                // Dismisses the notification when tapped
            .setContentIntent(pendingIntent)    // Tells it what to open when tapped

        // Get the system Notification Manager
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // For Android 8.0 (Oreo) and above, we MUST create a Notification Channel first
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Booking Updates",
                NotificationManager.IMPORTANCE_DEFAULT // Sets the priority level
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Finally, display the notification (ID 0 is used to overwrite old notifications)
        notificationManager.notify(0, notificationBuilder.build())
    }

    /**
     * Triggered when Firebase generates a new device token.
     * You need this token on your Django backend to send notifications to specific users.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // TODO (Critical for production):
        // You should make an API call to Django here to save this token to the user's profile.
        // Example: apiService.updateFcmToken(token)
    }
}