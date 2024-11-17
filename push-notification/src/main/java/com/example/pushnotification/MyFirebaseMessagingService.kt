package com.example.pushnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

  override fun onNewToken(token: String) {
    super.onNewToken(token)
// FCM token could change anytime, this method will be called,
// when there is new updated token for this user, so always update user's
// fcm token in your databse.
    // Store fcm token in user's record, so that later
    // could be used to send notification to same user.

    CoroutineScope(Dispatchers.IO).launch {
      setFcmToken(token)

    }

  }

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
// This method will be called everytime FCM service sends message.
    super.onMessageReceived(remoteMessage)
    remoteMessage.data.let {
      Log.e("pokemon", "hello data")
      showNotification(it["title"], it["body"])
    }
  }

  private fun showNotification(title: String?, body: String?) {
    val channelId = "default_channel"
    val channelName = "Default Channel"

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
      notificationManager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(this, channelId)
      .setContentTitle(title)
      .setContentText(body)
      .setSmallIcon(R.drawable.ic_launcher_foreground)
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      .build()

    notificationManager.notify(0, notification)
  }
}