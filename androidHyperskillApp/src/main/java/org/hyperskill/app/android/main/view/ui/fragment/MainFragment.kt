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
import org.hyperskill.app.android.debug.DebugScreen
import org.hyperskill.app.android.home.view.ui.screen.HomeScreen
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.profile.view.navigation.ProfileScreen
import org.hyperskill.app.android.study_plan.screen.StudyPlanScreen
import org.hyperskill.app.android.track.view.navigation.TrackScreen
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.debug.presentation.DebugFeature
import org.hyperskill.app.main.domain.analytic.AppClickedBottomNavigationItemHyperskillAnalyticEvent
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import ru.nobird.android.view.navigation.navigator.RetainedAppNavigator
import ru.nobird.android.view.navigation.ui.fragment.addBackNavigationDelegate

class MainFragment : Fragment(R.layout.fragment_main) {
    companion object {
        fun newInstance(): Fragment =
            MainFragment()
    }

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
                        Tabs.HOME ->
                            R.id.home_tab
                        Tabs.TRACK ->
                            R.id.track_tab
                        Tabs.STUDY_PLAN ->
                            R.id.study_plan_tab
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
            localCicerone.router.switch(HomeScreen)
        }

        with(viewBinding.mainBottomNavigation.menu) {
            findItem(R.id.debug_tab).isVisible =
                DebugFeature.isAvailable(buildKonfig)
            val isStudyPlanAvailable = StudyPlanScreenFeature.isAvailable(buildKonfig)
            findItem(R.id.study_plan_tab).isVisible = isStudyPlanAvailable
            findItem(R.id.track_tab).isVisible = !isStudyPlanAvailable
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
                    localCicerone.router.switch(HomeScreen)
                }
                R.id.track_tab -> {
                    localCicerone.router.switch(TrackScreen)
                }
                R.id.profile_tab -> {
                    localCicerone.router.switch(ProfileScreen(isInitCurrent = true))
                }
                R.id.debug_tab -> {
                    localCicerone.router.switch(DebugScreen)
                }
                R.id.study_plan_tab -> {
                    router.switch(StudyPlanScreen)
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
            R.id.study_plan_tab -> AppClickedBottomNavigationItemHyperskillAnalyticEvent.NavigationItem.STUDY_PLAN
            R.id.profile_tab -> AppClickedBottomNavigationItemHyperskillAnalyticEvent.NavigationItem.PROFILE
            R.id.debug_tab -> AppClickedBottomNavigationItemHyperskillAnalyticEvent.NavigationItem.DEBUG
            else -> null
        }
}