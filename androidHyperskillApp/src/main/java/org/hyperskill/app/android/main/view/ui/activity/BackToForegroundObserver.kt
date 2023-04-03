package org.hyperskill.app.android.main.view.ui.activity

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class BackToForegroundObserver(
    private val onBackToForeground: () -> Unit
) : DefaultLifecycleObserver {

    private var hasBeenStopped: Boolean = false

    override fun onStart(owner: LifecycleOwner) {
        if (hasBeenStopped) {
            onBackToForeground()
            hasBeenStopped = false
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        hasBeenStopped = true
    }

    override fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }
}