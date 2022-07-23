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
        self.onNewMessage(StepQuizFeatureMessageInitWithStep(step: self.step, forceUpdate: forceUpdate))
    }

    func syncReply(_ reply: Reply) {
        self.onNewMessage(StepQuizFeatureMessageSyncReply(reply: reply))
    }

    func doMainQuizAction() {
        guard let attemptLoadedState = self.state as? StepQuizFeatureStateAttemptLoaded,
              let submissionLoadedState = attemptLoadedState.submissionState as? StepQuizFeatureSubmissionStateLoaded,
              let reply = submissionLoadedState.submission.reply
        else {
            return
        }

        self.onNewMessage(StepQuizFeatureMessageCreateSubmissionClicked(step: self.step, reply: reply))
    }

    func makeViewData() -> StepQuizViewData {
        let attemptOrNil: Attempt? = {
            if let attemptLoadedState = self.state as? StepQuizFeatureStateAttemptLoaded {
                return attemptLoadedState.attempt
            }
            return nil
        }()
        return self.viewDataMapper.mapStepToViewData(self.step, attempt: attemptOrNil)
    }
}

// MARK: - StepQuizViewModel: StepQuizChildQuizDelegate -

extension StepQuizViewModel: StepQuizChildQuizDelegate {
    func handleChildQuizSync(reply: Reply) {
        self.syncReply(reply)
    }

    func handleChildQuizSubmitCurrentReply() {
        self.doMainQuizAction()
    }
}
