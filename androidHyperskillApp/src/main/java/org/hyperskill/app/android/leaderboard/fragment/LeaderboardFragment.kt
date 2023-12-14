package org.hyperskill.app.android.leaderboard.fragment

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.databinding.FragmentLeaderboardBinding
import org.hyperskill.app.android.databinding.LayoutGamificationToolbarBinding
import org.hyperskill.app.android.gamification_toolbar.view.ui.delegate.GamificationToolbarDelegate
import org.hyperskill.app.android.leaderboard.ui.LeaderboardScreen
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.leaderboard.presentation.LeaderboardViewModel
import org.hyperskill.app.leaderboard.screen.presentation.LeaderboardScreenFeature

class LeaderboardFragment : Fragment(R.layout.fragment_leaderboard) {

    companion object {
        fun newInstance(): LeaderboardFragment =
            LeaderboardFragment()
    }

    private val leaderboardViewBinding: FragmentLeaderboardBinding by viewBinding(FragmentLeaderboardBinding::bind)

    private var viewModelFactory: ReduxViewModelFactory? = null
    private val leaderboardViewModel: LeaderboardViewModel by viewModels { requireNotNull(viewModelFactory) }

    private val mainScreenRouter: MainScreenRouter =
        HyperskillApp.graph().navigationComponent.mainScreenCicerone.router

    private var gamificationToolbarDelegate: GamificationToolbarDelegate? = null

    private var fragmentWasResumed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
    }

    private fun injectComponent() {
        val leaderboardComponent = HyperskillApp.graph().buildPlatformLeaderboardComponent()
        viewModelFactory = leaderboardComponent.reduxViewModelFactory
        leaderboardViewModel.handleActions(this, onAction = ::onAction)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGamificationToolbar(leaderboardViewBinding.leaderboardAppBar)
        leaderboardViewBinding.leaderboardContent.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
            setContent {
                HyperskillTheme {
                    LeaderboardScreen(leaderboardViewModel)
                }
            }
        }
    }

    private fun initGamificationToolbar(viewBinding: LayoutGamificationToolbarBinding) {
        viewBinding.gamificationToolbar.title =
            requireContext().getString(org.hyperskill.app.R.string.leaderboard_title)
        gamificationToolbarDelegate = GamificationToolbarDelegate(
            lifecycleOwner = viewLifecycleOwner,
            context = requireContext(),
            viewBinding = viewBinding
        ) { message ->
            leaderboardViewModel.onNewMessage(message)
        }
        leaderboardViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .map { it.toolbarViewState }
            .distinctUntilChanged()
            .onEach { viewState ->
                gamificationToolbarDelegate?.render(viewState)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
        leaderboardViewModel.state
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .map { it.updatesInText }
            .distinctUntilChanged()
            .onEach { subtitle ->
                gamificationToolbarDelegate?.setSubtitle(subtitle)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onResume() {
        super.onResume()
        if (fragmentWasResumed) {
            leaderboardViewModel.onNewMessage(LeaderboardScreenFeature.Message.ScreenBecomesActive)
        } else {
            fragmentWasResumed = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        gamificationToolbarDelegate = null
    }

    private fun onAction(action: LeaderboardScreenFeature.Action.ViewAction) {
        when (action) {
            is LeaderboardScreenFeature.Action.ViewAction.GamificationToolbarViewAction -> {
                gamificationToolbarDelegate?.onAction(
                    action = action.viewAction,
                    mainScreenRouter = mainScreenRouter,
                    router = requireRouter(),
                    fragmentManager = childFragmentManager
                )
            }
            is LeaderboardScreenFeature.Action.ViewAction.LeaderboardWidgetViewAction ->
                when (action.viewAction) {
                    else -> {
                        // no op
                    }
                }
        }
    }
}