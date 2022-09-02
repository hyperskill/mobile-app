import shared
import SwiftUI

final class StepQuizViewModel: FeatureViewModel<
  StepQuizFeatureState,
  StepQuizFeatureMessage,
  StepQuizFeatureActionViewAction
> {
    let step: Step

    private let viewDataMapper: StepQuizViewDataMapper

    init(step: Step, viewDataMapper: StepQuizViewDataMapper, feature: Presentation_reduxFeature) {
        self.step = step
        self.viewDataMapper = viewDataMapper
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

    func onNotificationsGranted(_ granted: Bool) {
        if granted {
            onNewMessage(StepQuizFeatureMessageUserAgreedToEnableDailyReminders())
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
