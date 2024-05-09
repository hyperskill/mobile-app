package org.hyperskill.app.android.step.view.delegate

import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.github.terrakok.cicerone.Back
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.R
import ru.nobird.android.view.navigation.navigator.FragmentTransactionInterceptor
import ru.nobird.android.view.navigation.navigator.NestedAppNavigator
import ru.nobird.android.view.navigation.ui.fragment.BackNavigationDelegate
import ru.nobird.android.view.navigation.ui.fragment.addBackNavigationDelegate

class StepRouterDelegate(
    @IdRes containerId: Int,
    private val fragment: Fragment,
    onBackPressed: () -> Unit
): BackNavigationDelegate {

    private object StepFragmentTransactionInterceptor : FragmentTransactionInterceptor {
        override fun setupFragmentTransaction(
            screen: FragmentScreen,
            fragmentTransaction: FragmentTransaction,
            currentFragment: Fragment?,
            nextFragment: Fragment
        ) {
            fragmentTransaction.setCustomAnimations(
                /* enter = */ R.anim.slide_in,
                /* exit = */ R.anim.fade_out
            )
        }
    }

    private val localCicerone: Cicerone<Router> = Cicerone.create()

    val router: Router
        get() = localCicerone.router

    private val navigator: NestedAppNavigator by lazy(LazyThreadSafetyMode.NONE) {
        NestedAppNavigator(
            fragment.requireActivity(),
            containerId,
            fragment.childFragmentManager
        ).apply {
            interceptors.add(StepFragmentTransactionInterceptor)
        }
    }

    override val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
                navigator.applyCommands(arrayOf(Back()))
            }
        }

    init {
        fragment.lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        fragment.childFragmentManager.addOnBackStackChangedListener {
                            invalidateOnBackPressedCallback()
                        }
                        fragment.addBackNavigationDelegate(this)
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        localCicerone.getNavigatorHolder().setNavigator(navigator)
                    }
                    Lifecycle.Event.ON_PAUSE -> localCicerone.getNavigatorHolder().removeNavigator()
                    else -> {
                        // no op
                    }
                }
            }
        )
    }

    @Suppress("MagicNumber")
    override fun invalidateOnBackPressedCallback() {
        onBackPressedCallback.isEnabled =
            fragment.childFragmentManager.backStackEntryCount > 0
    }
}