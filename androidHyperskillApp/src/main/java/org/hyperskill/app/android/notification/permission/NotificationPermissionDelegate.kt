package org.hyperskill.app.android.notification.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class NotificationPermissionDelegate(
    private val fragment: Fragment
) {
    private var onResult: ((Result) -> Unit)? = null

    private val notificationPermissionCallback = fragment.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isNotificationPermissionGranted ->
        val result = when {
            isNotificationPermissionGranted -> Result.GRANTED
            !fragment.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> Result.DONT_ASK
            else -> Result.DENIED
        }
        onResult?.invoke(result)
    }

    fun requestNotificationPermission(onResult: (Result) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermissionGranted =
                ContextCompat.checkSelfPermission(
                    fragment.requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            if (notificationPermissionGranted) {
                onResult(Result.GRANTED)
            } else {
                this.onResult = onResult
                notificationPermissionCallback.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            onResult(Result.GRANTED)
        }
    }

    enum class Result {
        GRANTED,
        DENIED,
        DONT_ASK
    }
}