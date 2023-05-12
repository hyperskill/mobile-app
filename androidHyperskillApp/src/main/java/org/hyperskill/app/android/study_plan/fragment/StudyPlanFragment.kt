package org.hyperskill.app.android.study_plan.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.core.view.ui.setHyperskillColors
import org.hyperskill.app.android.core.view.ui.updateIsRefreshing
import org.hyperskill.app.android.databinding.FragmentStudyPlanBinding
import org.hyperskill.app.android.gamification_toolbar.view.ui.delegate.GamificationToolbarDelegate
import org.hyperskill.app.android.home.view.ui.screen.HomeScreen
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.problems_limit.view.ui.delegate.ProblemsLimitDelegate
import org.hyperskill.app.android.profile.view.navigation.ProfileScreen
import org.hyperskill.app.android.stage_implementation.view.dialog.UnsupportedStageBottomSheet
import org.hyperskill.app.android.stage_implementation.view.navigation.StageImplementationScreen
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.android.study_plan.delegate.StudyPlanWidgetDelegate
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.study_plan.presentation.StudyPlanScreenViewModel
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.screen.view.StudyPlanScreenViewState
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewState
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class StudyPlanFragment :
    Fragment(R.layout.fragment_study_plan),
    ReduxView<StudyPlanScreenViewState, StudyPlanScreenFeature.Action.ViewAction>,
    UnsupportedStageBottomSheet.Callback {

    companion object {
        fun newInstance(): StudyPlanFragment =
            StudyPlanFragment()
    }

    private val viewBinding: FragmentStudyPlanBinding by viewBinding(FragmentStudyPlanBinding::bind)

    private lateinit var viewModelFactory: ReduxViewModelFactory
    private val studyPlanViewModel: StudyPlanScreenViewModel by reduxViewModel(this) { viewModelFactory }

    private val mainScreenRouter: MainScreenRouter by lazy(LazyThreadSafetyMode.NONE) {
        HyperskillApp.graph().navigationComponent.mainScreenCicerone.router
    }

    private var gamificationToolbarDelegate: GamificationToolbarDelegate? = null
    private var problemsLimitDelegate: ProblemsLimitDelegate? = null
    private var studyPlanWidgetDelegate: StudyPlanWidgetDelegate? = null

    private var fragmentWasResumed = false

    override fun onResume() {
        super.onResume()
        if (fragmentWasResumed) {
            studyPlanViewModel.onNewMessage(StudyPlanScreenFeature.Message.ScreenBecomesActive)
        } else {
            fragmentWasResumed = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
        studyPlanWidgetDelegate = StudyPlanWidgetDelegate(
            context = requireContext(),
            onRetryContentLoadingClicked = {
                studyPlanViewModel.onNewMessage(StudyPlanScreenFeature.Message.RetryContentLoading)
            },
            onNewMessage = {
                studyPlanViewModel.onNewMessage(StudyPlanScreenFeature.Message.StudyPlanWidgetMessage(it))
            }
        )
    }

    private fun injectComponents() {
        val studyPlanScreeComponent =
            HyperskillApp.graph().buildPlatformStudyPlanScreenComponent()
        viewModelFactory = studyPlanScreeComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initGamificationToolbarDelegate()
        initProblemsLimitDelegate()
        initSwipeRefresh()
        studyPlanWidgetDelegate?.setup(viewBinding.studyPlanRecycler, viewBinding.studyPlanError)
    }

    private fun initGamificationToolbarDelegate() {
        viewBinding.studyPlanAppBar.gamificationToolbar.title =
            requireContext().getString(org.hyperskill.app.R.string.study_plan_title)
        gamificationToolbarDelegate = GamificationToolbarDelegate(
            viewLifecycleOwner,
            requireContext(),
            viewBinding.studyPlanAppBar
        ) { message ->
            studyPlanViewModel.onNewMessage(StudyPlanScreenFeature.Message.GamificationToolbarMessage(message))
        }
    }

    private fun initProblemsLimitDelegate() {
        problemsLimitDelegate = ProblemsLimitDelegate(
            viewBinding = viewBinding.studyPlanProblemsLimit,
            onNewMessage = {
                studyPlanViewModel.onNewMessage(StudyPlanScreenFeature.Message.ProblemsLimitMessage(it))
            }
        )
        problemsLimitDelegate?.setup()
    }

    private fun initSwipeRefresh() {
        with(viewBinding.studyPlanSwipeRefresh) {
            setHyperskillColors()
            setOnRefreshListener {
                studyPlanViewModel.onNewMessage(StudyPlanScreenFeature.Message.PullToRefresh)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        studyPlanWidgetDelegate?.cleanup()
        gamificationToolbarDelegate = null
        problemsLimitDelegate?.cleanup()
        problemsLimitDelegate = null
    }

    override fun onDestroy() {
        studyPlanWidgetDelegate = null
        super.onDestroy()
    }

    override fun render(state: StudyPlanScreenViewState) {
        renderSwipeRefresh(state)
        gamificationToolbarDelegate?.render(state.toolbarState)
        gamificationToolbarDelegate?.setSubtitle(state.trackTitle)
        problemsLimitDelegate?.render(state.problemsLimitViewState)
        studyPlanWidgetDelegate?.render(state.studyPlanWidgetViewState)
    }

    private fun renderSwipeRefresh(state: StudyPlanScreenViewState) {
        with(viewBinding.studyPlanSwipeRefresh) {
            isEnabled = state.studyPlanWidgetViewState is StudyPlanWidgetViewState.Content
            updateIsRefreshing(state.isRefreshing)
        }
    }

    override fun onAction(action: StudyPlanScreenFeature.Action.ViewAction) {
        when (action) {
            is StudyPlanScreenFeature.Action.ViewAction.GamificationToolbarViewAction -> {
                when (action.viewAction) {
                    GamificationToolbarFeature.Action.ViewAction.ShowProfileTab -> {
                        mainScreenRouter.switch(ProfileScreen(isInitCurrent = true))
                    }
                }
            }
            is StudyPlanScreenFeature.Action.ViewAction.ProblemsLimitViewAction -> {}
            is StudyPlanScreenFeature.Action.ViewAction.StudyPlanWidgetViewAction -> {
                when (val viewAction = action.viewAction) {
                    is StudyPlanWidgetFeature.Action.ViewAction.NavigateTo.StageImplement -> {
                        requireRouter().navigateTo(
                            StageImplementationScreen(
                                projectId = viewAction.projectId,
                                stageId = viewAction.stageId
                            )
                        )
                    }
                    is StudyPlanWidgetFeature.Action.ViewAction.NavigateTo.StepScreen -> {
                        requireRouter().navigateTo(StepScreen(viewAction.stepRoute))
                    }
                    is StudyPlanWidgetFeature.Action.ViewAction.NavigateTo.Home -> {
                        mainScreenRouter.switch(HomeScreen)
                    }
                    is StudyPlanWidgetFeature.Action.ViewAction.ShowStageImplementUnsupportedModal -> {
                        UnsupportedStageBottomSheet.newInstance()
                            .showIfNotExists(childFragmentManager, UnsupportedStageBottomSheet.TAG)
                    }
                }
            }
        }
    }

    // UnsupportedStageBottomSheet.Callback methods

    override fun onShow() {
        studyPlanViewModel.onNewMessage(
            StudyPlanScreenFeature.Message.StudyPlanWidgetMessage(
                StudyPlanWidgetFeature.Message.StageImplementUnsupportedModalShownEventMessage
            )
        )
    }

    override fun onDismiss() {
        studyPlanViewModel.onNewMessage(
            StudyPlanScreenFeature.Message.StudyPlanWidgetMessage(
                StudyPlanWidgetFeature.Message.StageImplementUnsupportedModalHiddenEventMessage
            )
        )
    }

    override fun onHomeClick() {
        studyPlanViewModel.onNewMessage(
            StudyPlanScreenFeature.Message.StudyPlanWidgetMessage(
                StudyPlanWidgetFeature.Message.StageImplementUnsupportedModalGoToHomeClicked
            )
        )

        childFragmentManager
            .dismissDialogFragmentIfExists(UnsupportedStageBottomSheet.TAG)
    }
}