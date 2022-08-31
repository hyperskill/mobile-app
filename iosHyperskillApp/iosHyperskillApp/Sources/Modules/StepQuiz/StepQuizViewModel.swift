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
        self.onNewMessage(StepQuizFeatureMessageInitWithStep(step: step, forceUpdate: forceUpdate))
    }

    func syncReply(_ reply: Reply) {
        self.onNewMessage(StepQuizFeatureMessageSyncReply(reply: reply))
    }

    func doMainQuizAction() {
        guard let attemptLoadedState = state as? StepQuizFeatureStateAttemptLoaded,
              let submissionLoadedState = attemptLoadedState.submissionState as? StepQuizFeatureSubmissionStateLoaded,
              let reply = submissionLoadedState.submission.reply
        else {
            return
        }

        self.onNewMessage(StepQuizFeatureMessageCreateSubmissionClicked(step: step, reply: reply))
    }

    func doQuizRetryAction() {
        // TODO: Implement quiz retry
        print(#function)
    }

    func doQuizContinueAction() {
        onNewMessage(StepQuizFeatureMessageContinueClicked())
    }

    func makeViewData() -> StepQuizViewData {
        let attemptOrNil: Attempt? = {
            if let attemptLoadedState = self.state as? StepQuizFeatureStateAttemptLoaded {
                return attemptLoadedState.attempt
            }
            return nil
        }()
        return viewDataMapper.mapStepToViewData(step, attempt: attemptOrNil)
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
}
