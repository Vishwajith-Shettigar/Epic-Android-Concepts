package com.example.broadcast_event

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AirplaneModeReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    if (intent?.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
      val isAirplaneModeOn = intent.getBooleanExtra("state", false)
      AirplaneModeBus.airplaneModeState.value = isAirplaneModeOn
    }
  }
}
