import shared
import SwiftUI

final class StepQuizViewModel: FeatureViewModel<
  StepQuizFeatureState,
  StepQuizFeatureMessage,
  StepQuizFeatureActionViewAction
> {
    let step: Step

    private let viewDataMapper: StepQuizViewDataMapper

    private let notificationService: NotificationsService
    private let notificationsRegistrationService: NotificationsRegistrationService

    init(
        step: Step,
        viewDataMapper: StepQuizViewDataMapper,
        notificationService: NotificationsService,
        notificationsRegistrationService: NotificationsRegistrationService,
        feature: Presentation_reduxFeature
    ) {
        self.step = step
        self.viewDataMapper = viewDataMapper
        self.notificationService = notificationService
        self.notificationsRegistrationService = notificationsRegistrationService
        super.init(feature: feature)
    }

    func loadAttempt(forceUpdate: Bool = false) {
        onNewMessage(StepQuizFeatureMessageInitWithStep(step: step, forceUpdate: forceUpdate))
    }

    func syncReply(_ reply: Reply) {
        onNewMessage(StepQuizFeatureMessageSyncReply(reply: reply))
    }

    func doMainQuizAction() {
        guard let attemptLoadedState = state as? StepQuizFeatureStateAttemptLoaded,
              let submissionLoadedState = attemptLoadedState.submissionState as? StepQuizFeatureSubmissionStateLoaded,
              let reply = submissionLoadedState.submission.reply
        else {
            return
        }

        onNewMessage(StepQuizFeatureMessageCreateSubmissionClicked(step: step, reply: reply))
    }

    func doQuizRetryAction() {
        // TODO: Implement quiz retry
        logClickedRetryEvent()
    }

    func doQuizContinueAction() {
        onNewMessage(StepQuizFeatureMessageContinueClicked())
    }

    func makeViewData() -> StepQuizViewData {
        let attemptOrNil: Attempt? = {
            if let attemptLoadedState = state as? StepQuizFeatureStateAttemptLoaded {
                return attemptLoadedState.attempt
            }
            return nil
        }()
        return viewDataMapper.mapStepToViewData(step, attempt: attemptOrNil)
    }

    func handleDailyStudyRemindersPermissionRequestResult(isGranted: Bool) {
        if isGranted {
            Task(priority: .userInitiated) {
                let isNotificationPermissionGranted =
                  await notificationsRegistrationService.requestAuthorizationIfNeeded()

                await MainActor.run {
                    if isNotificationPermissionGranted {
                        onNewMessage(StepQuizFeatureMessageUserAgreedToEnableDailyReminders())
                        notificationService.scheduleDailyStudyReminderLocalNotifications(
                            analyticRoute: HyperskillAnalyticRoute.Learn.LearnStep(stepId: step.id)
                        )
                    } else {
                        onNewMessage(StepQuizFeatureMessageUserDeclinedToEnableDailyReminders())
                    }
                }
            }
        } else {
            onNewMessage(StepQuizFeatureMessageUserDeclinedToEnableDailyReminders())
        }
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(StepQuizFeatureMessageViewedEventMessage(stepId: step.id))
    }

    private func logClickedRetryEvent() {
        onNewMessage(StepQuizFeatureMessageClickedRetryEventMessage())
    }
}

// MARK: - StepQuizViewModel: StepQuizChildQuizDelegate -

extension StepQuizViewModel: StepQuizChildQuizDelegate {
    func handleChildQuizSync(reply: Reply) {
        syncReply(reply)
    }

    func handleChildQuizSubmitCurrentReply() {
        doMainQuizAction()
    }

    func handleChildQuizRetry() {
        doQuizRetryAction()
    }

    func handleChildQuizAnalyticEventMessage(_ message: StepQuizFeatureMessage) {
        onNewMessage(message)
    }
}
