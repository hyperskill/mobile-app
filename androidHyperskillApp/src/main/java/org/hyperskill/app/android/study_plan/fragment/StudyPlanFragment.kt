package org.hyperskill.app.android.study_plan.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import co.touchlab.kermit.Logger
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.logger
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.core.view.ui.setHyperskillColors
import org.hyperskill.app.android.core.view.ui.updateIsRefreshing
import org.hyperskill.app.android.databinding.FragmentStudyPlanBinding
import org.hyperskill.app.android.gamification_toolbar.view.ui.delegate.GamificationToolbarDelegate
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.main.view.ui.navigation.switch
import org.hyperskill.app.android.paywall.navigation.PaywallScreen
import org.hyperskill.app.android.stage_implementation.dialog.UnsupportedStageBottomSheet
import org.hyperskill.app.android.study_plan.delegate.LearningActivityTargetViewActionHandler
import org.hyperskill.app.android.study_plan.delegate.StudyPlanWidgetDelegate
import org.hyperskill.app.android.track_selection.list.navigation.TrackSelectionListScreen
import org.hyperskill.app.android.users_interview_widget.delegate.UsersInterviewCardDelegate
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.study_plan.presentation.StudyPlanScreenViewModel
import org.hyperskill.app.study_plan.screen.presentation.StudyPlanScreenFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.view.model.StudyPlanWidgetViewState
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListParams
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class StudyPlanFragment :
    Fragment(R.layout.fragment_study_plan),
    ReduxView<StudyPlanScreenFeature.ViewState, StudyPlanScreenFeature.Action.ViewAction>,
    UnsupportedStageBottomSheet.Callback {

    companion object {
        private const val LOG_TAG = "StudyPlanFragment"
        fun newInstance(): StudyPlanFragment =
            StudyPlanFragment()
    }

    private val viewBinding: FragmentStudyPlanBinding by viewBinding(FragmentStudyPlanBinding::bind)

    private lateinit var viewModelFactory: ReduxViewModelFactory
    private val studyPlanViewModel: StudyPlanScreenViewModel by reduxViewModel(this) { viewModelFactory }

    private val mainScreenRouter: MainScreenRouter by lazy(LazyThreadSafetyMode.NONE) {
        HyperskillApp.graph().navigationComponent.mainScreenCicerone.router
    }

    private val logger: Logger by logger(LOG_TAG)

    private var gamificationToolbarDelegate: GamificationToolbarDelegate? = null
    private var studyPlanWidgetDelegate: StudyPlanWidgetDelegate? = null
    private var usersInterviewCardDelegate: UsersInterviewCardDelegate? = null

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
        initUserQuestionnaireCardDelegate()
        initSwipeRefresh()
        studyPlanWidgetDelegate?.setup(viewBinding.studyPlanRecycler, viewBinding.studyPlanError)
    }

    private fun initGamificationToolbarDelegate() {
        viewBinding.studyPlanAppBar.gamificationToolbar.title =
            requireContext().getString(org.hyperskill.app.R.string.study_plan_title)
        gamificationToolbarDelegate = GamificationToolbarDelegate(
            viewLifecycleOwner,
            requireContext(),
            viewBinding.studyPlanAppBar,
            onChangeTrackClicked = {
                studyPlanViewModel.onNewMessage(StudyPlanScreenFeature.Message.ChangeTrackButtonClicked)
            },
            onNewMessage = { message ->
                studyPlanViewModel.onNewMessage(StudyPlanScreenFeature.Message.GamificationToolbarMessage(message))
            }
        )
    }

    private fun initUserQuestionnaireCardDelegate() {
        usersInterviewCardDelegate = UsersInterviewCardDelegate()
        usersInterviewCardDelegate?.setup(
            viewBinding.studyPlanUserQuestionnaire,
            viewLifecycleOwner,
            onNewMessage = studyPlanViewModel::onNewMessage
        )
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
        usersInterviewCardDelegate = null
    }

    override fun onDestroy() {
        studyPlanWidgetDelegate = null
        super.onDestroy()
    }

    override fun render(state: StudyPlanScreenFeature.ViewState) {
        renderSwipeRefresh(state)
        gamificationToolbarDelegate?.render(state.toolbarViewState)
        gamificationToolbarDelegate?.setSubtitle(state.trackTitle, R.drawable.ic_change_track)
        studyPlanWidgetDelegate?.render(state.studyPlanWidgetViewState)
        usersInterviewCardDelegate?.render(
            state.usersInterviewWidgetState,
            viewBinding.studyPlanUserQuestionnaire
        )
    }

    private fun renderSwipeRefresh(state: StudyPlanScreenFeature.ViewState) {
        with(viewBinding.studyPlanSwipeRefresh) {
            isEnabled = state.studyPlanWidgetViewState is StudyPlanWidgetViewState.Content
            updateIsRefreshing(state.isRefreshing)
        }
    }

    override fun onAction(action: StudyPlanScreenFeature.Action.ViewAction) {
        when (action) {
            is StudyPlanScreenFeature.Action.ViewAction.GamificationToolbarViewAction -> {
                gamificationToolbarDelegate?.onAction(
                    action = action.viewAction,
                    mainScreenRouter = mainScreenRouter,
                    router = requireRouter(),
                    fragmentManager = childFragmentManager
                )
            }
            is StudyPlanScreenFeature.Action.ViewAction.StudyPlanWidgetViewAction -> {
                when (val studyPlanWidgetViewAction = action.viewAction) {
                    is StudyPlanWidgetFeature.Action.ViewAction.NavigateTo.Home -> {
                        mainScreenRouter.switch(Tabs.TRAINING)
                    }
                    is StudyPlanWidgetFeature.Action.ViewAction.NavigateTo.LearningActivityTarget -> {
                        LearningActivityTargetViewActionHandler.handle(
                            fragment = this,
                            viewAction = studyPlanWidgetViewAction.viewAction
                        )
                    }
                    is StudyPlanWidgetFeature.Action.ViewAction.NavigateTo.Paywall -> {
                        requireRouter().navigateTo(
                            PaywallScreen(studyPlanWidgetViewAction.paywallTransitionSource)
                        )
                    }
                }
            }
            is StudyPlanScreenFeature.Action.ViewAction.UsersInterviewWidgetViewAction -> {
                usersInterviewCardDelegate?.handleActions(
                    fragment = this,
                    action = action.viewAction,
                    logger = logger
                )
            }
            StudyPlanScreenFeature.Action.ViewAction.NavigateTo.TrackSelectionScreen -> {
                requireRouter().navigateTo(
                    TrackSelectionListScreen(
                        TrackSelectionListParams()
                    )
                )
            }
            is StudyPlanScreenFeature.Action.ViewAction.NotificationDailyStudyReminderWidgetViewAction -> {
                // TODO: ALTAPPS-1347 handle notification daily study reminder widget view actions
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