package org.hyperskill.app.android.core.view.ui.navigation

import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import org.hyperskill.app.android.core.view.ui.fragment.parentOfType
import ru.nobird.android.view.navigation.router.RetainedRouter
import ru.nobird.android.view.navigation.ui.fragment.NavigationContainer

fun Fragment.requireAppRouter(): Router =
    requireNotNull(parentOfType(AppNavigationContainer::class.java)?.router) {
        "Fragment $this not attached to a AppNavigationContainer."
    }

fun Fragment.requireRouter(): Router =
    requireNotNull(parentOfType(NavigationContainer::class.java)?.router) {
        "Fragment $this not attached to a NavigationContainer."
    }

fun Fragment.requireMainRouter(): RetainedRouter =
    requireNotNull(parentOfType(MainNavigationContainer::class.java)?.router) {
        "Fragment $this not attached to a MainNavigationContainer."
    }
