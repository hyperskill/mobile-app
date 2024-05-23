import Combine
import shared
import SwiftUI

final class StepViewModel: FeatureViewModel<
  StepFeature.ViewState,
  StepFeatureMessage,
  StepFeatureActionViewAction
> {
    let stepRoute: StepRoute

    weak var stepQuizModuleInput: StepQuizInputProtocol?
    private var updateStepQuizSubscription: AnyCancellable?

    private let viewDataMapper: StepViewDataMapper

    var stepStateKs: StepFeatureStepStateKs { .init(state.stepState) }
    var stepToolbarViewStateKs: StepToolbarFeatureViewStateKs { .init(state.stepToolbarViewState) }

    var isStageImplement: Bool { stepRoute is StepRouteStageImplement }

    init(
        stepRoute: StepRoute,
        viewDataMapper: StepViewDataMapper,
        feature: Presentation_reduxFeature
    ) {
        self.stepRoute = stepRoute
        self.viewDataMapper = viewDataMapper

        super.init(feature: feature)

        onNewMessage(StepFeatureMessageInitialize(forceUpdate: false))
    }

    override func shouldNotifyStateDidChange(
        oldState: StepFeature.ViewState,
        newState: StepFeature.ViewState
    ) -> Bool {
        let shouldNotify = !oldState.isEqual(newState)

        if shouldNotify,
           oldState.stepState is StepFeatureStepStateData,
           newState.stepState is StepFeatureStepStateData {
            updateStepQuizSubscription = objectWillChange.sink { [weak self] in
                guard let strongSelf = self else {
                    return
                }

                strongSelf.updateStepQuizSubscription?.cancel()
                strongSelf.updateStepQuizSubscription = nil

                strongSelf.updateStepQuiz()
            }
        }

        return shouldNotify
    }

    func doScreenShowedAction() {
        onNewMessage(StepFeatureMessageScreenShowed())
    }

    func doScreenHiddenAction() {
        onNewMessage(StepFeatureMessageScreenHidden())
    }

    func doRetryLoadStep() {
        onNewMessage(StepFeatureMessageInitialize(forceUpdate: true))
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

    func logSpacebotClickedEvent() {
        onNewMessage(
            StepFeatureMessageStepToolbarMessage(message: StepToolbarFeatureMessageSpacebotClicked())
        )
    }

    private func updateStepQuiz() {
        mainScheduler.schedule { [weak self] in
            guard let strongSelf = self,
                  case .data(let stepFeatureStateData) = strongSelf.stepStateKs else {
                return
            }

            strongSelf.stepQuizModuleInput?.update(
                isPracticingLoading: stepFeatureStateData.stepCompletionState.isPracticingLoading
            )
        }
    }
}

// MARK: - StepViewModel: StepQuizOutputProtocol -

extension StepViewModel: StepQuizOutputProtocol {
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
    func topicCompletedModalViewControllerDidTapGoToStudyPlanButton(
        _ viewController: TopicCompletedModalViewController
    ) {
        #warning("todo")
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageTopicCompletedModalGoToStudyPlanClicked()
            )
        )

        viewController.dismiss(animated: true)
    }

    func topicCompletedModalViewControllerDidTapContinueWithNextTopicButton(
        _ viewController: TopicCompletedModalViewController
    ) {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageTopicCompletedModalContinueNextTopicClicked()
            )
        )

        viewController.dismiss(animated: true)
    }

    func topicCompletedModalViewControllerDidAppear(_ viewController: TopicCompletedModalViewController) {
    }

    func topicCompletedModalViewControllerDidDisappear(_ viewController: TopicCompletedModalViewController) {
    }
}

// MARK: - StepViewModel: ProblemOfDaySolvedModalViewControllerDelegate -

extension StepViewModel: ProblemOfDaySolvedModalViewControllerDelegate {
    func problemOfDaySolvedModalViewControllerDidAppear(_ viewController: ProblemOfDaySolvedModalViewController) {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageDailyStepCompletedModalShownEventMessage()
            )
        )
    }

    func problemOfDaySolvedModalViewControllerDidDisappear(_ viewController: ProblemOfDaySolvedModalViewController) {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageDailyStepCompletedModalHiddenEventMessage()
            )
        )
    }

    func problemOfDaySolvedModalViewControllerDidTapGoBackButton(
        _ viewController: ProblemOfDaySolvedModalViewController
    ) {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageProblemOfDaySolvedModalGoBackClicked()
            )
        )
    }

    func problemOfDaySolvedModalViewControllerDidTapShareStreakButton(
        _ viewController: ProblemOfDaySolvedModalViewController,
        streak: Int
    ) {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageProblemOfDaySolvedModalShareStreakClicked(streak: Int32(streak))
            )
        )
    }
}

// MARK: - StepViewModel: ShareStreakModalViewControllerDelegate -

extension StepViewModel: ShareStreakModalViewControllerDelegate {
    func shareStreakModalViewControllerDidAppear(_ viewController: ShareStreakModalViewController, streak: Int) {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageShareStreakModalShownEventMessage(streak: Int32(streak))
            )
        )
    }

    func shareStreakModalViewControllerDidDisappear(_ viewController: ShareStreakModalViewController, streak: Int) {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageShareStreakModalHiddenEventMessage(streak: Int32(streak))
            )
        )
    }

    func shareStreakModalViewControllerDidTapShareButton(
        _ viewController: ShareStreakModalViewController,
        streak: Int
    ) {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageShareStreakModalShareClicked(streak: Int32(streak))
            )
        )
    }

    func shareStreakModalViewControllerDidTapNoThanksButton(
        _ viewController: ShareStreakModalViewController,
        streak: Int
    ) {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageShareStreakModalNoThanksClickedEventMessage(streak: Int32(streak))
            )
        )
    }
}
