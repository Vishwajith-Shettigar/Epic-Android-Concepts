package com.example.protobuf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.proto.Post

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val post = Post.newBuilder().apply {
      this.id = 1
      this.name = "Shinchan"
      this.email = "welcomebackhome@nohara.com"
    }.build()

    val nameTv = this.findViewById<TextView>(R.id.name)
    nameTv.text = post.name
    Log.d("protobuf", post.toString())
  }
}
