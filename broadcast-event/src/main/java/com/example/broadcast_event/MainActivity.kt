package com.example.broadcast_event

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.broadcast_event.databinding.MainActivityBinding

const val CUSTOM_BROADCAST_ACTION = "com.example.broadcast_event_COUNTER_EPIC"

class MainActivity : AppCompatActivity() {
  private lateinit var binding: MainActivityBinding

  lateinit var airplaneModeReceiver: AirplaneModeReceiver

  lateinit var counterBroadcastReceiver: CounterBroadcastReceiver

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = MainActivityBinding.inflate(layoutInflater)
    setContentView(binding.root)
    airplaneModeReceiver = AirplaneModeReceiver()
    counterBroadcastReceiver = CounterBroadcastReceiver()

    val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
    registerReceiver(airplaneModeReceiver, intentFilter)

    val intentFilter2 = IntentFilter(CUSTOM_BROADCAST_ACTION)
    registerReceiver(counterBroadcastReceiver, intentFilter2, RECEIVER_EXPORTED)

    AirplaneModeBus.airplaneModeState.observe(this) {
      if (it)
        showToast(this, "Airplane mode on")
      else
        showToast(this, "Airplane mode off")
    }

    CounterBus.CounterState.observe(this) {
      binding.cntLabel.text = it.toString()
    }

    var count = 0

    binding.btn.setOnClickListener {
      count++
      val intent = Intent(CUSTOM_BROADCAST_ACTION).apply {
        putExtra("count", count.toString())
      }
      sendBroadcast(intent)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    unregisterReceiver(airplaneModeReceiver)
  }
}

fun showToast(context: Context, message: String) {
  Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
