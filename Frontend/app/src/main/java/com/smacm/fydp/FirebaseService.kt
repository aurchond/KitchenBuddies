package com.smacm.fydp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random


// ID for channel
private const val CHANNEL_ID = "my_channel"

class FirebaseService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        // called whenever this device receives a message (when subscribed to TOPIC)
        super.onMessageReceived(message)

        // show notification
        val intent = Intent(this, PastRecipesFragment::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt() // make sure we always have a different ID for each notification

        // need to make a channel for the notifications from Android Oreo onwards
        createNotificationChannel(notificationManager)

        // all activities that are not the calling activity are cleared from the top of the call stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)

        // create actual notification
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"]) // get title of message received from Firebase
            .setContentText(message.data["message"]) // same thing as above but get actual message
            .setSmallIcon(R.drawable.ic_baseline_notifications_24) // set icon from the drawable folder
            .setAutoCancel(true) // notification is deleted when clicked
            .setContentIntent(pendingIntent)
            .build()

        // display the notification using the notification manager
        notificationManager.notify(notificationID, notification)

    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "My channel description"
            enableLights(true)
            lightColor = Color.GREEN
        }

       notificationManager.createNotificationChannel(channel)
    }
}