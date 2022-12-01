package org.hyperskill.app.android.home.view.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.SharedResources
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.launchUrl
import org.hyperskill.app.android.core.view.ui.dialog.LoadingProgressDialogFragment
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentHomeBinding
import org.hyperskill.app.android.problem_of_day.view.delegate.ProblemOfDayCardFormDelegate
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.android.streak.view.delegate.StreakCardFormDelegate
import org.hyperskill.app.android.topics_repetitions.view.delegate.TopicsRepetitionCardFormDelegate
import org.hyperskill.app.android.topics_repetitions.view.screen.TopicsRepetitionScreen
import org.hyperskill.app.home.presentation.HomeFeature
import org.hyperskill.app.home.presentation.HomeViewModel
import org.hyperskill.app.streak.domain.model.Streak
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.android.view.base.ui.extension.snackbar
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
    private val topicsRepetitionDelegate: TopicsRepetitionCardFormDelegate by lazy(LazyThreadSafetyMode.NONE) {
        TopicsRepetitionCardFormDelegate()
    }

    private val onForegroundObserver =
        object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                homeViewModel.onNewMessage(HomeFeature.Message.Initialize(forceUpdate = true))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
        requireActivity().lifecycle.addObserver(onForegroundObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewStateDelegate()

        viewBinding.homeScreenError.tryAgain.setOnClickListener {
            homeViewModel.onNewMessage(HomeFeature.Message.Initialize(forceUpdate = false))
        }

        viewBinding.homeScreenKeepLearningInWebButton.setOnClickListener {
            homeViewModel.onNewMessage(HomeFeature.Message.ClickedContinueLearningOnWeb)
        }

//        viewBinding.homeOpenStepButton.setOnClickListener {
//            val stepId = viewBinding.homeOpenStepInputEditText.text.toString().toLongOrNull()
//            if (stepId == null) {
//                view.snackbar("Insert a valid number", Snackbar.LENGTH_SHORT)
//            } else {
//                requireRouter().navigateTo(StepScreen(stepId))
//            }
//        }

        homeViewModel.onNewMessage(HomeFeature.Message.Initialize(forceUpdate = false))
        homeViewModel.onNewMessage(HomeFeature.Message.ViewedEventMessage)
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().lifecycle.removeObserver(onForegroundObserver)
    }

    private fun injectComponents() {
        val homeComponent = HyperskillApp.graph().buildHomeComponent()
        val platformHomeComponent = HyperskillApp.graph().buildPlatformHomeComponent(homeComponent)
        viewModelFactory = platformHomeComponent.reduxViewModelFactory
    }

    private fun onProblemOfDayCardActionButtonClicked(stepId: Long) {
        homeViewModel.onNewMessage(HomeFeature.Message.ClickedProblemOfDayCardEventMessage)
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

    override fun onAction(action: HomeFeature.Action.ViewAction) {
        when (action) {
            is HomeFeature.Action.ViewAction.OpenUrl -> {
                requireContext().launchUrl(action.url)
            }
            is HomeFeature.Action.ViewAction.ShowGetMagicLinkError -> {
                viewBinding.root.snackbar(SharedResources.strings.common_error.resourceId)
            }
            is HomeFeature.Action.ViewAction.NavigateTo.TopicsRepetitionsScreen -> {
                requireRouter().navigateTo(TopicsRepetitionScreen(action.recommendedRepetitionsCount))
            }
            else -> {
                // no op
            }
        }
    }

    override fun render(state: HomeFeature.State) {
        viewStateDelegate.switchState(state)
        if (state is HomeFeature.State.Content) {
            if (state.isLoadingMagicLink) {
                LoadingProgressDialogFragment.newInstance()
                    .showIfNotExists(childFragmentManager, LoadingProgressDialogFragment.TAG)
            } else {
                childFragmentManager.dismissDialogFragmentIfExists(LoadingProgressDialogFragment.TAG)
            }
            renderStreakCardDelegate(state.streak)
            renderProblemOfDayCardDelegate(state.problemOfDayState)
            renderTopicsRepetition(state.recommendedRepetitionsCount)
        }
    }

    private fun renderStreakCardDelegate(streak: Streak?) {
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

    private fun renderProblemOfDayCardDelegate(state: HomeFeature.ProblemOfDayState) {
        problemOfDayCardFormDelegate = ProblemOfDayCardFormDelegate(
            requireContext(),
            viewBinding.homeScreenProblemOfDayCard,
            state,
            ::onProblemOfDayCardActionButtonClicked
        )

        if (state is HomeFeature.ProblemOfDayState.Solved || state is HomeFeature.ProblemOfDayState.Empty) {
            viewBinding.homeScreenKeepLearningInWebButton.visibility = View.VISIBLE
        } else {
            viewBinding.homeScreenKeepLearningInWebButton.visibility = View.GONE
        }
    }

    private fun renderTopicsRepetition(topicsToRepeatCount: Int) {
        viewBinding.homeScreenTopicsRepetitionCard.root.setOnClickListener {
            homeViewModel.onNewMessage(HomeFeature.Message.ClickedTopicsRepetitionsCard)
        }
        topicsRepetitionDelegate.render(
            requireContext(),
            viewBinding.homeScreenTopicsRepetitionCard,
            topicsToRepeatCount
        )
    }
}