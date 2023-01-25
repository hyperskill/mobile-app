import shared
import SwiftUI

final class StepViewModel: FeatureViewModel<StepFeatureState, StepFeatureMessage, StepFeatureActionViewAction> {
    let stepRoute: StepRoute

    private let viewDataMapper: StepViewDataMapper

    var stateKs: StepFeatureStateKs { .init(state) }

    init(
        stepRoute: StepRoute,
        viewDataMapper: StepViewDataMapper,
        feature: Presentation_reduxFeature
    ) {
        self.stepRoute = stepRoute
        self.viewDataMapper = viewDataMapper
        super.init(feature: feature)
    }

    override func shouldNotifyStateDidChange(oldState: StepFeatureState, newState: StepFeatureState) -> Bool {
        StepFeatureStateKs(oldState) != StepFeatureStateKs(newState)
    }

    func loadStep(forceUpdate: Bool = false) {
        onNewMessage(StepFeatureMessageInitialize(forceUpdate: forceUpdate))
    }

    func makeViewData(_ step: Step) -> StepViewData {
        viewDataMapper.mapStepToViewData(step)
    }

    func doStartPracticing(currentStep: Step) {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageStartPracticingClicked(currentStep: currentStep)
            )
        )
    }

    func doGoToHomeScreenAction() {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageTopicCompletedModalGoToHomeScreenClicked()
            )
        )
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(StepFeatureMessageViewedEventMessage())
    }

    func logTopicCompletedModalShownEvent() {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageTopicCompletedModalShownEventMessage()
            )
        )
    }

    func logTopicCompletedModalHiddenEvent() {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageTopicCompletedModalHiddenEventMessage()
            )
        )
    }
}

extension StepViewModel: StepQuizOutputProtocol {
    var isPracticingLoading: Bool {
        switch stateKs {
        case .data(let data):
            return data.stepCompletionState.isPracticingLoading
        default:
            return false
        }
    }

    func doContinuePracticing(currentStep: Step) {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageContinuePracticingClicked(currentStep: currentStep)
            )
        )
    }
}
