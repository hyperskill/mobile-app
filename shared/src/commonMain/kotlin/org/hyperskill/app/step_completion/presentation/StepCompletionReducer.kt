package org.hyperskill.app.step_completion.presentation

import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionClickedContinueHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.StepCompletionClickedStartPracticingHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.topic_completed_modal.StepCompletionTopicCompletedModalClickedGoToHomeScreenHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.topic_completed_modal.StepCompletionTopicCompletedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.topic_completed_modal.StepCompletionTopicCompletedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Action
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.ContinueButtonAction
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Message
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.State
import org.hyperskill.app.step_completion.domain.analytic.daily_notifications_notice.StepCompletionHiddenDailyNotificationsNoticeHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.daily_notifications_notice.StepCompletionShownDailyNotificationsNoticeHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.daily_step_completed_modal.StepCompletionDailyStepCompletedModalClickedGoBackHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.daily_step_completed_modal.StepCompletionDailyStepCompletedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_completion.domain.analytic.daily_step_completed_modal.StepCompletionDailyStepCompletedModalShownHyperskillAnalyticEvent
import ru.nobird.app.presentation.redux.reducer.StateReducer

class StepCompletionReducer(private val stepRoute: StepRoute) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.ContinuePracticingClicked ->
                if (!state.isPracticingLoading) {
                    val analyticEvent = StepCompletionClickedContinueHyperskillAnalyticEvent(
                        route = stepRoute.analyticRoute
                    )
                    state.copy(
                        isPracticingLoading = state.continueButtonAction is ContinueButtonAction.FetchNextStepQuiz ||
                            state.continueButtonAction is ContinueButtonAction.CheckTopicCompletion
                    ) to setOf(
                        Action.LogAnalyticEvent(analyticEvent),
                        when (state.continueButtonAction) {
                            ContinueButtonAction.NavigateToBack -> Action.ViewAction.NavigateTo.Back
                            ContinueButtonAction.NavigateToHomeScreen -> Action.ViewAction.NavigateTo.HomeScreen
                            ContinueButtonAction.FetchNextStepQuiz -> Action.FetchNextRecommendedStep(state.currentStep)
                            ContinueButtonAction.CheckTopicCompletion -> state.currentStep.topic?.let {
                                Action.CheckTopicCompletionStatus(it)
                            } ?: Action.ViewAction.NavigateTo.Back
                        }
                    )
                } else {
                    null
                }
            is Message.StartPracticingClicked ->
                if (!state.isPracticingLoading) {
                    state.copy(isPracticingLoading = true) to setOf(
                        Action.LogAnalyticEvent(
                            StepCompletionClickedStartPracticingHyperskillAnalyticEvent(
                                route = stepRoute.analyticRoute
                            )
                        ),
                        Action.FetchNextRecommendedStep(state.currentStep)
                    )
                } else {
                    null
                }
            is Message.FetchNextRecommendedStepResult.Success ->
                state.copy(isPracticingLoading = false) to setOf(Action.ViewAction.ReloadStep(message.newStepRoute))
            is Message.FetchNextRecommendedStepResult.Error ->
                state.copy(isPracticingLoading = false) to setOf(
                    Action.ViewAction.ShowStartPracticingError(message.errorMessage)
                )
            is Message.CheckTopicCompletionStatus.Completed ->
                state.copy(
                    continueButtonAction = ContinueButtonAction.NavigateToHomeScreen,
                    isPracticingLoading = false
                ) to setOf(Action.ViewAction.ShowTopicCompletedModal(message.modalText))
            is Message.CheckTopicCompletionStatus.Uncompleted ->
                state.copy(isPracticingLoading = false) to emptySet()
            is Message.CheckTopicCompletionStatus.Error ->
                state.copy(
                    continueButtonAction = ContinueButtonAction.CheckTopicCompletion,
                    isPracticingLoading = false
                ) to emptySet()
            is Message.TopicCompletedModalGoToHomeScreenClicked ->
                state to setOf(
                    Action.ViewAction.NavigateTo.HomeScreen,
                    Action.LogAnalyticEvent(
                        StepCompletionTopicCompletedModalClickedGoToHomeScreenHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute
                        )
                    )
                )
            is Message.RequestDailyStudyRemindersPermission ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        StepCompletionShownDailyNotificationsNoticeHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    ),
                    Action.ViewAction.RequestDailyStudyRemindersPermission
                )
            is Message.RequestDailyStudyRemindersPermissionResult -> {
                val analyticEvent = StepCompletionHiddenDailyNotificationsNoticeHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute,
                    isAgreed = message.isGranted
                )
                state to setOf(
                    Action.RequestDailyStudyRemindersPermissionResult(message.isGranted),
                    Action.LogAnalyticEvent(analyticEvent)
                )
            }
            is Message.StepSolved ->
                if (!state.isPracticingLoading &&
                    stepRoute is StepRoute.Learn &&
                    message.stepId == state.currentStep.id &&
                    state.currentStep.topic != null
                ) {
                    state.copy(isPracticingLoading = true) to setOf(
                        Action.CheckTopicCompletionStatus(state.currentStep.topic)
                    )
                } else {
                    null
                }
            is Message.TopicCompletedModalShownEventMessage -> {
                val event = StepCompletionTopicCompletedModalShownHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute
                )
                state to setOf(Action.LogAnalyticEvent(event))
            }
            is Message.TopicCompletedModalHiddenEventMessage -> {
                val event = StepCompletionTopicCompletedModalHiddenHyperskillAnalyticEvent(
                    route = stepRoute.analyticRoute
                )
                state to setOf(Action.LogAnalyticEvent(event))
            }
            is Message.DailyStepCompletedModalShownEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        StepCompletionDailyStepCompletedModalShownHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    )
                )
            is Message.DailyStepCompletedModalHiddenEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        StepCompletionDailyStepCompletedModalHiddenHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    )
                )
            is Message.ShowProblemOfDaySolvedModal ->
                state to setOf(Action.ViewAction.ShowProblemOfDaySolvedModal(message.earnedGemsText))
            is Message.ProblemOfDaySolvedModalGoBackClicked ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        StepCompletionDailyStepCompletedModalClickedGoBackHyperskillAnalyticEvent(
                            stepRoute.analyticRoute
                        )
                    ),
                    Action.ViewAction.NavigateTo.Back
                )
        } ?: (state to emptySet())
}