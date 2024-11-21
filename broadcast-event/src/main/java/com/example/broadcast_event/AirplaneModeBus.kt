package com.example.broadcast_event

import androidx.lifecycle.MutableLiveData

object AirplaneModeBus {
  val airplaneModeState = MutableLiveData<Boolean>()
}

object CounterBus {
  val CounterState = MutableLiveData<Int>()
}
