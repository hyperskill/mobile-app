package org.hyperskill.app.android.home.view.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.SharedResources
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.openUrl
import org.hyperskill.app.android.core.view.ui.dialog.LoadingProgressDialogFragment
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.core.view.ui.setHyperskillColors
import org.hyperskill.app.android.core.view.ui.updateIsRefreshing
import org.hyperskill.app.android.databinding.FragmentHomeBinding
import org.hyperskill.app.android.gamification_toolbar.view.ui.delegate.GamificationToolbarDelegate
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.next_learning_activity.view.delegate.NextLearningActivityDelegate
import org.hyperskill.app.android.problem_of_day.view.delegate.ProblemOfDayCardFormDelegate
import org.hyperskill.app.android.problems_limit.view.ui.delegate.ProblemsLimitDelegate
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.android.topics_repetitions.view.delegate.TopicsRepetitionCardFormDelegate
import org.hyperskill.app.android.topics_repetitions.view.screen.TopicsRepetitionScreen
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.home.presentation.HomeFeature
import org.hyperskill.app.home.presentation.HomeViewModel
import org.hyperskill.app.problems_limit.domain.model.ProblemsLimitScreen
import org.hyperskill.app.problems_limit.view.mapper.ProblemsLimitViewStateMapper
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.base.ui.extension.showIfNotExists
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
    private lateinit var dateFormatter: SharedDateFormatter

    private val viewBinding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val homeViewModel: HomeViewModel by reduxViewModel(this) { viewModelFactory }
    private val homeViewStateDelegate: ViewStateDelegate<HomeFeature.HomeState> = ViewStateDelegate()

    private var problemsLimitDelegate: ProblemsLimitDelegate? = null
    private var problemsLimitViewStateMapper: ProblemsLimitViewStateMapper? = null

    private val problemOfDayCardFormDelegate: ProblemOfDayCardFormDelegate by lazy(LazyThreadSafetyMode.NONE) {
        ProblemOfDayCardFormDelegate(
            onCardClicked = ::onProblemOfDayCardActionButtonClicked,
            onReloadClick = {
                homeViewModel.onNewMessage(HomeFeature.Message.ClickedProblemOfDayCardReload)
            }
        )
    }
    private val topicsRepetitionDelegate: TopicsRepetitionCardFormDelegate by lazy(LazyThreadSafetyMode.NONE) {
        TopicsRepetitionCardFormDelegate()
    }

    private val nextLearningActivityDelegate: NextLearningActivityDelegate by lazy(LazyThreadSafetyMode.NONE) {
        NextLearningActivityDelegate { nextLearningActivityMessage ->
            homeViewModel.onNewMessage(
                HomeFeature.Message.NextLearningActivityWidgetMessage(nextLearningActivityMessage)
            )
        }
    }

    private var gamificationToolbarDelegate: GamificationToolbarDelegate? = null

    private val onForegroundObserver =
        object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                homeViewModel.onNewMessage(HomeFeature.Message.Initialize(forceUpdate = true))
            }
        }

    private val mainScreenRouter: MainScreenRouter =
        HyperskillApp.graph().navigationComponent.mainScreenCicerone.router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
        requireActivity().lifecycle.addObserver(onForegroundObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewStateDelegate()
        initGamificationToolbarDelegate()
        initProblemsLimitDelegate()
        problemOfDayCardFormDelegate.setup(viewBinding.homeScreenProblemOfDayCard)
        nextLearningActivityDelegate.setup(requireContext(), viewBinding.homeNextLearningActivity)
        with(viewBinding) {
            homeScreenSwipeRefreshLayout.setHyperskillColors()
            homeScreenSwipeRefreshLayout.setOnRefreshListener {
                homeViewModel.onNewMessage(HomeFeature.Message.PullToRefresh)
            }
            homeScreenError.tryAgain.setOnClickListener {
                homeViewModel.onNewMessage(HomeFeature.Message.Initialize(forceUpdate = true))
            }
            homeScreenKeepLearningInWebButton.setOnClickListener {
                homeViewModel.onNewMessage(HomeFeature.Message.ClickedContinueLearningOnWeb)
            }

            homeScreenTopicsRepetitionCard.root.setOnClickListener {
                homeViewModel.onNewMessage(HomeFeature.Message.ClickedTopicsRepetitionsCard)
            }
        }

        homeViewModel.onNewMessage(HomeFeature.Message.Initialize(forceUpdate = false))
        homeViewModel.onNewMessage(HomeFeature.Message.ViewedEventMessage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        gamificationToolbarDelegate = null
        problemsLimitDelegate?.cleanup()
        problemsLimitDelegate = null
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().lifecycle.removeObserver(onForegroundObserver)
    }

    private fun injectComponents() {
        val homeComponent = HyperskillApp.graph().buildHomeComponent()
        val platformHomeComponent = HyperskillApp.graph().buildPlatformHomeComponent(homeComponent)
        val problemsLimitComponent = HyperskillApp.graph().buildProblemsLimitComponent(ProblemsLimitScreen.HOME)
        viewModelFactory = platformHomeComponent.reduxViewModelFactory
        problemsLimitViewStateMapper = problemsLimitComponent.problemsLimitViewStateMapper
        dateFormatter = HyperskillApp.graph().commonComponent.dateFormatter
    }

    private fun onProblemOfDayCardActionButtonClicked(stepId: Long) {
        homeViewModel.onNewMessage(HomeFeature.Message.ClickedProblemOfDayCardEventMessage)
        requireRouter().navigateTo(StepScreen(StepRoute.LearnDaily(stepId)))
    }

    private fun initViewStateDelegate() {
        with(homeViewStateDelegate) {
            addState<HomeFeature.HomeState.Idle>()
            addState<HomeFeature.HomeState.Loading>(
                viewBinding.homeScreenContainer,
                viewBinding.homeScreenSkeleton.root,
                viewBinding.homeScreenAppBar.root,
                viewBinding.homeKeepLearningInWebButtonSkeleton
            )
            addState<HomeFeature.HomeState.NetworkError>(viewBinding.homeScreenError.root)
            addState<HomeFeature.HomeState.Content>(
                viewBinding.homeScreenAppBar.root,
                viewBinding.homeScreenContainer,
                viewBinding.homeScreenKeepPracticingTextView,
                viewBinding.homeScreenProblemOfDayCard.root,
                viewBinding.homeScreenTopicsRepetitionCard.root,
                viewBinding.homeScreenKeepLearningInWebButton,
            )
        }
    }

    private fun initGamificationToolbarDelegate() {
        viewBinding.homeScreenAppBar.gamificationCollapsingToolbarLayout.title =
            requireContext().getString(org.hyperskill.app.R.string.home_title)
        gamificationToolbarDelegate = GamificationToolbarDelegate(
            viewLifecycleOwner,
            requireContext(),
            viewBinding.homeScreenAppBar
        ) { message ->
            homeViewModel.onNewMessage(HomeFeature.Message.GamificationToolbarMessage(message))
        }
    }

    private fun initProblemsLimitDelegate() {
        problemsLimitDelegate = ProblemsLimitDelegate(
            viewBinding = viewBinding.homeProblemsLimit,
            onNewMessage = {
                homeViewModel.onNewMessage(HomeFeature.Message.ProblemsLimitMessage(it))
            }
        )
        problemsLimitDelegate?.setup()
    }

    override fun onAction(action: HomeFeature.Action.ViewAction) {
        when (action) {
            is HomeFeature.Action.ViewAction.OpenUrl -> {
                requireContext().openUrl(action.url)
            }
            is HomeFeature.Action.ViewAction.ShowGetMagicLinkError -> {
                viewBinding.root.snackbar(SharedResources.strings.common_error.resourceId)
            }
            is HomeFeature.Action.ViewAction.NavigateTo.TopicsRepetitionsScreen -> {
                requireRouter().navigateTo(TopicsRepetitionScreen())
            }
            is HomeFeature.Action.ViewAction.GamificationToolbarViewAction ->
                gamificationToolbarDelegate?.onAction(
                    action = action.viewAction,
                    mainScreenRouter = mainScreenRouter,
                    router = requireRouter()
                )
            else -> {
                // no op
            }
        }
    }

    override fun render(state: HomeFeature.State) {
        homeViewStateDelegate.switchState(state.homeState)

        renderSwipeRefresh(state)

        val homeState = state.homeState
        if (homeState is HomeFeature.HomeState.Content) {
            renderMagicLinkState(homeState.isLoadingMagicLink)
            renderProblemOfDay(viewBinding, homeState.problemOfDayState, homeState.isFreemiumEnabled)
            renderTopicsRepetition(homeState.repetitionsState, homeState.isFreemiumEnabled)
        }

        gamificationToolbarDelegate?.render(state.toolbarState)
        problemsLimitViewStateMapper?.let { mapper ->
            problemsLimitDelegate?.render(mapper.mapState(state.problemsLimitState))
        }
        nextLearningActivityDelegate.render(requireContext(), state.nextLearningActivityWidgetState)
    }

    private fun renderSwipeRefresh(state: HomeFeature.State) {
        with(viewBinding.homeScreenSwipeRefreshLayout) {
            isEnabled = state.homeState is HomeFeature.HomeState.Content
            updateIsRefreshing(state.isRefreshing)
        }
    }

    private fun renderMagicLinkState(isLoadingMagicLink: Boolean) {
        if (isLoadingMagicLink) {
            LoadingProgressDialogFragment.newInstance()
                .showIfNotExists(childFragmentManager, LoadingProgressDialogFragment.TAG)
        } else {
            childFragmentManager
                .dismissDialogFragmentIfExists(LoadingProgressDialogFragment.TAG)
        }
    }

    private fun renderProblemOfDay(
        viewBinding: FragmentHomeBinding,
        state: HomeFeature.ProblemOfDayState,
        isFreemiumEnabled: Boolean
    ) {
        problemOfDayCardFormDelegate.render(
            dateFormatter = dateFormatter,
            binding = viewBinding.homeScreenProblemOfDayCard,
            state = state,
            isFreemiumEnabled = isFreemiumEnabled
        )
        viewBinding.homeScreenKeepLearningInWebButton.isVisible =
            state is HomeFeature.ProblemOfDayState.Solved || state is HomeFeature.ProblemOfDayState.Empty
    }

    private fun renderTopicsRepetition(
        repetitionsState: HomeFeature.RepetitionsState,
        isFreemiumEnabled: Boolean
    ) {
        viewBinding.homeScreenTopicsRepetitionCard.root.isVisible =
            repetitionsState is HomeFeature.RepetitionsState.Available
        if (repetitionsState is HomeFeature.RepetitionsState.Available) {
            topicsRepetitionDelegate.render(
                context = requireContext(),
                binding = viewBinding.homeScreenTopicsRepetitionCard,
                recommendedRepetitionsCount = repetitionsState.recommendedRepetitionsCount,
                isFreemiumEnabled = isFreemiumEnabled
            )
        }
    }
}