package org.hyperskill.app.android.main.view.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.Cicerone
import org.hyperskill.app.analytic.domain.model.Analytic
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.navigation.MainNavigationContainer
import org.hyperskill.app.android.databinding.FragmentMainBinding
import org.hyperskill.app.android.debug_menu.navigation.DebugScreen
import org.hyperskill.app.android.home.view.ui.screen.HomeScreen
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.profile.view.navigation.ProfileScreen
import org.hyperskill.app.android.track.view.navigation.TrackScreen
import org.hyperskill.app.main.domain.analytic.AppClickedBottomNavigationItemHyperskillAnalyticEvent
import ru.nobird.android.view.navigation.navigator.RetainedAppNavigator
import ru.nobird.android.view.navigation.router.RetainedRouter
import ru.nobird.android.view.navigation.ui.fragment.addBackNavigationDelegate

class MainFragment : Fragment(R.layout.fragment_main), MainNavigationContainer {
    companion object {
        fun newInstance(): Fragment =
            MainFragment()
    }

    private val viewBinding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    private val localCicerone: Cicerone<RetainedRouter> = Cicerone.create(RetainedRouter())
    override val router: RetainedRouter = localCicerone.router

    private val navigator by lazy(LazyThreadSafetyMode.NONE) {
        RetainedAppNavigator(
            R.id.mainFragmentNavigationContainer,
            childFragmentManager,
            mode = RetainedAppNavigator.Mode.ATTACH_DETACH,
            onScreenChanged = { screenKey ->
                viewBinding.mainBottomNavigation.selectedItemId =
                    when (Tabs.valueOf(screenKey)) {
                        Tabs.HOME ->
                            R.id.home_tab
                        Tabs.TRACK ->
                            R.id.track_tab
                        Tabs.PROFILE ->
                            R.id.profile_tab
                        Tabs.DEBUG ->
                            R.id.debug_tab
                    }
            }
        )
    }

    private lateinit var analytic: Analytic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
        addBackNavigationDelegate(navigator)
    }

    private fun injectComponents() {
        val analyticComponent = HyperskillApp.graph().analyticComponent
        analytic = analyticComponent.analyticInteractor
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null && childFragmentManager.fragments.isEmpty()) {
            router.switch(HomeScreen)
        }

        viewBinding.mainBottomNavigation.setOnItemSelectedListener { item ->
            logClickedBottomNavigationItemEvent(
                oldItemId = viewBinding.mainBottomNavigation.selectedItemId,
                newItemId = item.itemId
            )

            // Guard in order to avoid endless loop when pressing "Back"
            if (item.itemId == viewBinding.mainBottomNavigation.selectedItemId) {
                return@setOnItemSelectedListener true
            }

            when (item.itemId) {
                R.id.home_tab -> {
                    router.switch(HomeScreen)
                }
                R.id.track_tab -> {
                    router.switch(TrackScreen)
                }
                R.id.profile_tab -> {
                    router.switch(ProfileScreen(isInitCurrent = true))
                }
                R.id.debug_tab -> {
                    router.switch(DebugScreen)
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun onResume() {
        super.onResume()
        localCicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        localCicerone.getNavigatorHolder().removeNavigator()
        super.onPause()
    }

    private fun logClickedBottomNavigationItemEvent(oldItemId: Int, newItemId: Int) {
        val oldNavigationItem = resolveAnalyticNavigationItem(oldItemId)
        val newNavigationItem = resolveAnalyticNavigationItem(newItemId)

        if (oldNavigationItem == null || newNavigationItem == null) {
            return
        }

        val event = AppClickedBottomNavigationItemHyperskillAnalyticEvent(oldNavigationItem, newNavigationItem)
        analytic.reportEvent(event)
    }

    private fun resolveAnalyticNavigationItem(itemId: Int): AppClickedBottomNavigationItemHyperskillAnalyticEvent.NavigationItem? =
        when (itemId) {
            R.id.home_tab -> AppClickedBottomNavigationItemHyperskillAnalyticEvent.NavigationItem.HOME
            R.id.track_tab -> AppClickedBottomNavigationItemHyperskillAnalyticEvent.NavigationItem.TRACK
            R.id.profile_tab -> AppClickedBottomNavigationItemHyperskillAnalyticEvent.NavigationItem.PROFILE
            else -> null
        }
}