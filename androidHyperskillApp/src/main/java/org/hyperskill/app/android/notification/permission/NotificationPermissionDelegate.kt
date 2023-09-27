package org.hyperskill.app.android.notification.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class NotificationPermissionDelegate(
    private val fragment: Fragment,
    private val onResult: ((Result) -> Unit)
) {

    companion object {
        private const val SAVED_STATE_PROVIDER_KEY = "NOTIFICATION_PERMISSION_DELEGATE"
        private const val SHOULD_SHOW_REQUEST_PERMISSION_RATIONALE_BEFORE =
            "SHOULD_SHOW_REQUEST_PERMISSION_RATIONALE_BEFORE"
    }

    private var shouldShowRequestPermissionRationaleBefore: Boolean = false

    init {
        this.shouldShowRequestPermissionRationaleBefore =
            fragment.savedStateRegistry.consumeRestoredStateForKey(SAVED_STATE_PROVIDER_KEY)
                ?.getBoolean(SHOULD_SHOW_REQUEST_PERMISSION_RATIONALE_BEFORE, false)
                ?: false
        fragment.savedStateRegistry.registerSavedStateProvider(SAVED_STATE_PROVIDER_KEY) {
            bundleOf(SHOULD_SHOW_REQUEST_PERMISSION_RATIONALE_BEFORE to shouldShowRequestPermissionRationaleBefore)
        }
    }

    private val notificationPermissionCallback = fragment.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isNotificationPermissionGranted ->
        val result = when {
            isNotificationPermissionGranted -> Result.GRANTED
            // User denied permission another time
            // System request was shown for the last time
            shouldShowRequestPermissionRationaleBefore &&
                !fragment.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> Result.DENIED
            !fragment.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> Result.DONT_ASK
            else -> Result.DENIED
        }
        onResult.invoke(result)
    }

    fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermissionGranted =
                ContextCompat.checkSelfPermission(
                    fragment.requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            if (notificationPermissionGranted) {
                onResult(Result.GRANTED)
            } else {
                shouldShowRequestPermissionRationaleBefore =
                    fragment.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
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