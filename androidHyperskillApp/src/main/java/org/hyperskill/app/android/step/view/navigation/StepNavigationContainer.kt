package org.hyperskill.app.android.step.view.navigation

import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import org.hyperskill.app.android.core.view.ui.fragment.parentOfType

interface StepNavigationContainer {
    val router: Router
}

fun Fragment.requireStepRouter(): Router =
    requireNotNull(parentOfType(StepNavigationContainer::class.java)?.router) {
        "Fragment $this not attached to a StepNavigationContainer."
    }