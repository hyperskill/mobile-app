package org.hyperskill.app.android.core.extensions

import android.app.Activity
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

/**
 * Sets a callback to be invoked when a screenshot is captured on the screen.
 * Works only on Android 14 and higher.
 *
 * @param block The code block to be executed when a screenshot is captured.
 */
inline fun Fragment.doOnScreenShootCaptured(
    crossinline block: () -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        val callBack = Activity.ScreenCaptureCallback {
            block()
        }
        lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        requireActivity().registerScreenCaptureCallback(
                            /* executor = */ requireActivity().mainExecutor,
                            /* callback = */ callBack
                        )
                    }
                    Lifecycle.Event.ON_STOP -> {
                        requireActivity().unregisterScreenCaptureCallback(callBack)
                    }
                    else -> {
                        // no op
                    }
                }
            }
        )
    }
}

