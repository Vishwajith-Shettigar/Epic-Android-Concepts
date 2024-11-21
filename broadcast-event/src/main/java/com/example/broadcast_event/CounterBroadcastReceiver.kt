package com.example.broadcast_event

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class CounterBroadcastReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    if (intent?.action==CUSTOM_BROADCAST_ACTION) {
      val count = intent.getStringExtra("count")
      if (count != null) {
        CounterBus.CounterState.value = count.toInt()
      }
    }
  }
}
