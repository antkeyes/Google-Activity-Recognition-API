package com.example.googleactivityrecognitionapi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionResult

class ActivityRecognitionBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_PROCESS_UPDATES = "com.example.activityrecognitionapp.ACTION_PROCESS_UPDATES"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_PROCESS_UPDATES) {
            val result = ActivityTransitionResult.extractResult(intent)
            result?.let {
                for (event in it.transitionEvents) {
                    val activityType = event.activityType
                    val activityTransition = event.transitionType
                    val localIntent = Intent("activity-update")
                    localIntent.putExtra("type", activityType)
                    localIntent.putExtra("transition", activityTransition)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent)
                }
            }
        }
    }
}
