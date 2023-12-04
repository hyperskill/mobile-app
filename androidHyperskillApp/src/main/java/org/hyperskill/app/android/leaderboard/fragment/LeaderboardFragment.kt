package org.hyperskill.app.android.leaderboard.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
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

    private var gamificationToolbarDelegate: GamificationToolbarDelegate? = null

    private val mainScreenRouter: MainScreenRouter =
        HyperskillApp.graph().navigationComponent.mainScreenCicerone.router

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
        leaderboardViewBinding.leaderboardContent.setContent {
            HyperskillTheme {
                LeaderboardScreen()
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
                    router = requireRouter()
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