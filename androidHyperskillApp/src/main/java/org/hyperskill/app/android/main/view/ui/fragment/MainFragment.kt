package org.hyperskill.app.android.main.view.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.Cicerone
import org.hyperskill.app.analytic.domain.model.Analytic
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentMainBinding
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.main.view.ui.navigation.switch
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.debug.presentation.DebugFeature
import org.hyperskill.app.main.domain.analytic.AppClickedBottomNavigationItemHyperskillAnalyticEvent
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.navigation.navigator.RetainedAppNavigator
import ru.nobird.android.view.navigation.ui.fragment.addBackNavigationDelegate

class MainFragment : Fragment(R.layout.fragment_main) {
    companion object {
        fun newInstance(initialTab: Tabs): Fragment =
            MainFragment().apply {
                this.initialTab = initialTab
            }
    }

    private var initialTab: Tabs by argument()

    private val viewBinding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    private val localCicerone: Cicerone<MainScreenRouter> =
        HyperskillApp.graph().navigationComponent.mainScreenCicerone

    private val navigator by lazy(LazyThreadSafetyMode.NONE) {
        RetainedAppNavigator(
            R.id.mainFragmentNavigationContainer,
            childFragmentManager,
            mode = RetainedAppNavigator.Mode.ATTACH_DETACH,
            onScreenChanged = { screenKey ->
                viewBinding.mainBottomNavigation.selectedItemId =
                    when (Tabs.valueOf(screenKey)) {
                        Tabs.TRAINING ->
                            R.id.training_tab
                        Tabs.STUDY_PLAN ->
                            R.id.study_plan_tab
                        Tabs.LEADERBOARD ->
                            R.id.leaderboard_tab
                        Tabs.PROFILE ->
                            R.id.profile_tab
                        Tabs.DEBUG ->
                            R.id.debug_tab
                    }
            }
        )
    }

    private lateinit var analytic: Analytic
    private val buildKonfig: BuildKonfig by lazy(LazyThreadSafetyMode.NONE) {
        HyperskillApp.graph().commonComponent.buildKonfig
    }

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
            localCicerone.router.switch(initialTab)
        }

        viewBinding.mainBottomNavigation.menu.findItem(R.id.debug_tab).isVisible =
            DebugFeature.isAvailable(buildKonfig)

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
                R.id.training_tab -> {
                    localCicerone.router.switch(Tabs.TRAINING)
                }
                R.id.profile_tab -> {
                    localCicerone.router.switch(Tabs.PROFILE)
                }
                R.id.debug_tab -> {
                    localCicerone.router.switch(Tabs.DEBUG)
                }
                R.id.study_plan_tab -> {
                    localCicerone.router.switch(Tabs.STUDY_PLAN)
                }
                R.id.leaderboard_tab -> {
                    localCicerone.router.switch(Tabs.LEADERBOARD)
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

    fun onLeaderboardTabVisibilityChanged(isVisible: Boolean) {
        viewBinding.mainBottomNavigation.menu.findItem(R.id.leaderboard_tab)
            .isVisible = isVisible
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

    private fun resolveAnalyticNavigationItem(
        itemId: Int
    ): AppClickedBottomNavigationItemHyperskillAnalyticEvent.NavigationItem? =
        when (itemId) {
            R.id.training_tab -> AppClickedBottomNavigationItemHyperskillAnalyticEvent.NavigationItem.HOME
            R.id.study_plan_tab -> AppClickedBottomNavigationItemHyperskillAnalyticEvent.NavigationItem.STUDY_PLAN
            R.id.profile_tab -> AppClickedBottomNavigationItemHyperskillAnalyticEvent.NavigationItem.PROFILE
            R.id.debug_tab -> AppClickedBottomNavigationItemHyperskillAnalyticEvent.NavigationItem.DEBUG
            R.id.leaderboard_tab -> AppClickedBottomNavigationItemHyperskillAnalyticEvent.NavigationItem.LEADERBOARD
            else -> null
        }
}