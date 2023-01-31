import shared
import SwiftUI

final class StepViewModel: FeatureViewModel<StepFeatureState, StepFeatureMessage, StepFeatureActionViewAction> {
    let stepRoute: StepRoute

    weak var stepQuizModuleInput: StepQuizInputProtocol?

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
        let newStateKs = StepFeatureStateKs(newState)

        let shouldNotify = StepFeatureStateKs(oldState) != newStateKs

        if shouldNotify, case .data(let stepFeatureStateData) = stateKs {
            stepQuizModuleInput?.updateIsPracticingLoading(
                isPracticingLoading: stepFeatureStateData.stepCompletionState.isPracticingLoading
            )
        }

        return shouldNotify
    }

    func loadStep(forceUpdate: Bool = false) {
        onNewMessage(StepFeatureMessageInitialize(forceUpdate: forceUpdate))
    }

    func makeViewData(_ step: Step) -> StepViewData {
        viewDataMapper.mapStepToViewData(step)
    }

    func doStartPracticing() {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageStartPracticingClicked()
            )
        )
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(StepFeatureMessageViewedEventMessage())
    }
}

extension StepViewModel: StepQuizOutputProtocol {
    // updatePracticeLoading
    var isPracticingLoading: Bool {
        switch stateKs {
        case .data(let data):
            return data.stepCompletionState.isPracticingLoading
        default:
            return false
        }
    }

    func stepQuizDidRequestContinue() {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageContinuePracticingClicked()
            )
        )
    }
}

// MARK: - StepViewModel: TopicCompletedModalViewControllerDelegate -

extension StepViewModel: TopicCompletedModalViewControllerDelegate {
    func topicCompletedModalViewControllerDidTapGoToHomescreenButton(
        _ viewController: TopicCompletedModalViewController
    ) {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageTopicCompletedModalGoToHomeScreenClicked()
            )
        )

        viewController.dismiss(animated: true)
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
