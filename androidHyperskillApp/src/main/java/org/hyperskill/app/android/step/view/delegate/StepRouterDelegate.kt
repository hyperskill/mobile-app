package org.hyperskill.app.android.step.view.delegate

import androidx.activity.addCallback
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.R
import ru.nobird.android.view.navigation.navigator.FragmentTransactionInterceptor
import ru.nobird.android.view.navigation.navigator.NestedAppNavigator

class StepRouterDelegate(
    @IdRes containerId: Int,
    fragment: Fragment,
    onBackPressed: () -> Unit
) {
    private val localCicerone: Cicerone<Router> = Cicerone.create()

    val router: Router
        get() = localCicerone.router

    private var navigator: NestedAppNavigator? = null

    init {
        fragment.lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        val activity = fragment.requireActivity()
                        val navigator = createNavigator(activity, containerId, fragment.childFragmentManager)
                        this.navigator = navigator
                        activity.onBackPressedDispatcher.addCallback(fragment) {
                            onBackPressed()
                            navigator.onBackPressedCallback.handleOnBackPressed()
                        }
                        navigator.invalidateOnBackPressedCallback()
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        navigator?.let {
                            localCicerone.getNavigatorHolder().setNavigator(it)
                        }
                    }
                    Lifecycle.Event.ON_PAUSE -> localCicerone.getNavigatorHolder().removeNavigator()
                    Lifecycle.Event.ON_DESTROY -> {
                        navigator = null
                    }
                    else -> {
                        // no op
                    }
                }
            }
        )
    }

    private fun createNavigator(
        activity: FragmentActivity,
        @IdRes containerId: Int,
        fragmentManager: FragmentManager
    ): NestedAppNavigator =
        NestedAppNavigator(
            activity,
            containerId,
            fragmentManager
        ).apply {
            interceptors.add(
                object : FragmentTransactionInterceptor {
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
            )
        }
}