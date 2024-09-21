package com.example.navigation_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

class NavigationExample : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      Greeting("Welcome to Jetpack Compose!")

    }
  }
}

@Composable
fun Greeting(name: String) {
  Text(text = "Hello, $name!")
}
