package com.example.googleactivityrecognitionapi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionEvent
import com.google.android.gms.location.ActivityTransitionResult

class ActivityRecognitionBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent)
            for (event in result!!.transitionEvents) {
                when (event.transitionType) {
                    ActivityTransition.ACTIVITY_TRANSITION_ENTER -> {
                        Toast.makeText(context, "Device picked up", Toast.LENGTH_SHORT).show()
                    }
                    ActivityTransition.ACTIVITY_TRANSITION_EXIT -> {
                        Toast.makeText(context, "Device put down", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
