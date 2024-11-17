package com.example.pushnotification

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pushnotification.databinding.MainActivityBinding
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.initialize
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// Assuming this is userId.
const val userId = "673bdkw72k"

class MainActivity : AppCompatActivity() {
  private lateinit var binding: MainActivityBinding

  val retrofit = Retrofit.Builder()
    .baseUrl("http://server_base_url")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

  val notificationApi by lazy {
    retrofit.create(NotificationApi::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = MainActivityBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.btn.setOnClickListener {
      try {
        lifecycleScope.launch {
          withContext(Dispatchers.IO) {
            notificationApi.sendNotification(
              NotificationRequest(
                listOf(userId), title = "You have got new notification",
                body = "Epic Android Concepts Series, ep 3"
              )
            )
          }
        }
      } catch (e: Exception) {
        Log.e("pokemon",e.toString())
      }
    }

    lifecycleScope.launch {
      withContext(Dispatchers.IO) {
        val token: String = FirebaseMessaging.getInstance().token.await()
        setFcmToken(token)
      }
    }
  }
}

suspend fun setFcmToken(token: String) {
  try {
    FirebaseFirestore.getInstance().collection("users").document(userId).set(
      mapOf(
        "fcmToken" to token
      )
    ).addOnSuccessListener {

    }.addOnFailureListener {
    }
  } catch (e: Exception) {
  }
}

data class NotificationRequest(
  val userIds: List<String>,
  val title: String,
  val body: String
)

interface NotificationApi {
  @POST("notifications/send")
  suspend fun sendNotification(@Body notificationRequest: NotificationRequest): Response<Unit>
}
