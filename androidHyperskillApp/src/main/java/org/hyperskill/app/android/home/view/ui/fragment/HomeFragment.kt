package org.hyperskill.app.android.home.view.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentHomeBinding
import org.hyperskill.app.android.problem_of_day.view.delegate.ProblemOfDayCardFormDelegate
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.android.streak.view.delegate.StreakCardFormDelegate
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.home.presentation.HomeFeature
import org.hyperskill.app.home.presentation.HomeViewModel
import org.hyperskill.app.streak.domain.model.Streak
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class HomeFragment :
    Fragment(R.layout.fragment_home),
    ReduxView<HomeFeature.State, HomeFeature.Action.ViewAction> {
    companion object {
        fun newInstance(): Fragment =
            HomeFragment()
    }

    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewBinding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val homeViewModel: HomeViewModel by reduxViewModel(this) { viewModelFactory }
    private val viewStateDelegate: ViewStateDelegate<HomeFeature.State> = ViewStateDelegate()

    private lateinit var problemOfDayCardFormDelegate: ProblemOfDayCardFormDelegate
    private lateinit var streakCardFormDelegate: StreakCardFormDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewStateDelegate()

        viewBinding.homeScreenError.tryAgain.setOnClickListener {
            homeViewModel.onNewMessage(HomeFeature.Message.Init(forceUpdate = false))
        }

        viewBinding.homeScreenKeepLearningInWebButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(BuildKonfig.BASE_URL)
            startActivity(intent)
        }

        homeViewModel.onNewMessage(HomeFeature.Message.Init(forceUpdate = false))
        homeViewModel.onNewMessage(HomeFeature.Message.HomeViewedEventMessage)
    }

    private fun injectComponents() {
        val homeComponent = HyperskillApp.graph().buildHomeComponent()
        val platformHomeComponent = HyperskillApp.graph().buildPlatformHomeComponent(homeComponent)
        viewModelFactory = platformHomeComponent.reduxViewModelFactory
    }

    private fun onProblemOfDayCardActionButtonClicked(stepId: Long) {
        requireRouter().navigateTo(StepScreen(stepId))
    }

    private fun initViewStateDelegate() {
        with(viewStateDelegate) {
            addState<HomeFeature.State.Idle>()
            addState<HomeFeature.State.Loading>(viewBinding.homeScreenProgress)
            addState<HomeFeature.State.NetworkError>(viewBinding.homeScreenError.root)
            addState<HomeFeature.State.Content>(viewBinding.homeScreenContainer)
        }
    }

    private fun setupStreakCardDelegate(streak: Streak?) {
        if (streak == null) {
            viewBinding.homeScreenStreakCard.root.visibility = View.GONE
            return
        }

        streakCardFormDelegate = StreakCardFormDelegate(
            requireContext(),
            viewBinding.homeScreenStreakCard,
            streak
        )
    }

    private fun setupProblemOfDayCardDelegate(state: HomeFeature.ProblemOfDayState) {
        problemOfDayCardFormDelegate = ProblemOfDayCardFormDelegate(
            requireContext(),
            viewBinding.homeScreenProblemOfDayCard,
            state,
            ::onProblemOfDayCardActionButtonClicked
        )
    }

    override fun onAction(action: HomeFeature.Action.ViewAction) {
    }

    override fun render(state: HomeFeature.State) {
        viewStateDelegate.switchState(state)
        when (state) {
            is HomeFeature.State.Content -> {
                setupStreakCardDelegate(state.streak)
                setupProblemOfDayCardDelegate(state.problemOfDayState)
            }
        }
    }
}