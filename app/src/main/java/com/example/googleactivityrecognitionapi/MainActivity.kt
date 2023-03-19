package com.example.googleactivityrecognitionapi

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.os.Bundle
import android.widget.TextView
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionClient
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity

private const val PERMISSIONS_REQUEST_CODE = 1

class MainActivity : AppCompatActivity() {

    private lateinit var activityRecognitionClient: ActivityRecognitionClient
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_CODE
            )
        } else {
            setupActivityRecognition()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupActivityRecognition()
            }
        }
    }

    private fun setupActivityRecognition() {
        activityRecognitionClient = ActivityRecognition.getClient(this)

        val transitions = listOf(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_FOOT)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build(),
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_FOOT)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build()
        )

        val request = ActivityTransitionRequest(transitions)
        val pendingIntent = getPendingIntent()

        activityRecognitionClient.requestActivityTransitionUpdates(request, pendingIntent)
            .addOnSuccessListener {
                textView.text = "Successfully registered for activity recognition updates."
            }
            .addOnFailureListener { e ->
                textView.text = "Failed to register for activity recognition updates: ${e.localizedMessage}"
            }
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this, ActivityRecognitionBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}
