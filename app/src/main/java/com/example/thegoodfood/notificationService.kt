package com.example.thegoodfood

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


class notificationService : FirebaseMessagingService() {
    lateinit var notif_title: String
    lateinit var notif_body: String
    public var CHANNEL_ID = "myNotification"
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage?.notification != null) {
            Log.d("firebase Notification", remoteMessage!!.notification!!.body.toString())
            notif_title = remoteMessage!!.notification!!.title.toString()
            notif_title = remoteMessage!!.notification!!.body.toString()
            val db = FirebaseDatabase.getInstance().reference
            db.child("users")
                .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .child("notification")
                .child(UUID.randomUUID().toString())
                .setValue(notif_body)
            createNotification(notif_title, notif_body)
        }
    }

    private fun createNotification(notif_title: String, notif_body: String) {
        var intent = Intent(this, Homepage::class.java)
        var pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        var notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(notif_title)
            .setContentText(notif_body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        var manage =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channel = NotificationChannel(
                CHANNEL_ID,
                "notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manage.createNotificationChannel(channel)
        }
        manage.notify(0, notif.build())
    }
}