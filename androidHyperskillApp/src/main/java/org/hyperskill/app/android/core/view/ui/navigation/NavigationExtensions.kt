package org.hyperskill.app.android.core.view.ui.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.github.terrakok.cicerone.Router
import org.hyperskill.app.android.core.view.ui.fragment.parentOfType
import ru.nobird.android.view.navigation.router.RetainedRouter
import ru.nobird.android.view.navigation.ui.fragment.NavigationContainer
import ru.nobird.app.core.model.safeCast
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen

fun Fragment.requireAppRouter(): Router =
    requireNotNull(parentOfType(AppNavigationContainer::class.java)?.router) {
        "Fragment $this not attached to a AppNavigationContainer."
    }

fun Fragment.requireRouter(): Router =
    requireNotNull(parentOfType(NavigationContainer::class.java)?.router) {
        "Fragment $this not attached to a NavigationContainer."
    }

/**
 * Is used to retrieve router from [MainScreen]s child fragment
 * */
fun Fragment.requireMainRouter(): RetainedRouter =
    requireNotNull(parentOfType(MainNavigationContainer::class.java)?.router) {
        "Fragment $this not attached to a MainNavigationContainer."
    }

/**
 * I used to retrieve router from non [MainScreen]s child fragment.
 * */
fun FragmentManager.requireMainRouter(): RetainedRouter =
    requireNotNull(findFragmentByTag(MainNavigationContainer.ContainerTag)?.safeCast<MainNavigationContainer>()?.router) {
        "FragmentManger $this does not contain Fragment implemented MainNavigationContainer"
    }
