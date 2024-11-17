package com.example.pushnotification

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pushnotification.databinding.MainActivityBinding
import com.google.firebase.firestore.FirebaseFirestore
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

  val retrofit by lazy {
    Retrofit.Builder()
      .baseUrl("http://server_base_url")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

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
          try {
            withContext(Dispatchers.IO) {
              notificationApi.sendNotification(
                NotificationRequest(
                  listOf(userId), title = "You have got new notification",
                  body = "Epic Android Concepts Series, ep 3"
                )
              )
            }
          }catch (e: Exception) {
            Toast.makeText(applicationContext,e.message.toString(),Toast.LENGTH_SHORT).show()
            Log.e("e",e.toString())
          }
        }
      } catch (e: Exception) {
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
    ).await()
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
