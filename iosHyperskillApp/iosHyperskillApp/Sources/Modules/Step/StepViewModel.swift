import Combine
import shared
import SwiftUI

final class StepViewModel: FeatureViewModel<StepFeatureState, StepFeatureMessage, StepFeatureActionViewAction> {
    let stepRoute: StepRoute

    weak var stepQuizModuleInput: StepQuizInputProtocol?
    private var updateStepQuizSubscription: AnyCancellable?

    private let viewDataMapper: StepViewDataMapper

    private let notificationService: NotificationsService
    private let notificationsRegistrationService: NotificationsRegistrationService

    var stateKs: StepFeatureStateKs { .init(state) }

    init(
        stepRoute: StepRoute,
        viewDataMapper: StepViewDataMapper,
        notificationService: NotificationsService,
        notificationsRegistrationService: NotificationsRegistrationService,
        feature: Presentation_reduxFeature
    ) {
        self.stepRoute = stepRoute
        self.viewDataMapper = viewDataMapper
        self.notificationService = notificationService
        self.notificationsRegistrationService = notificationsRegistrationService
        super.init(feature: feature)
    }

    override func shouldNotifyStateDidChange(oldState: StepFeatureState, newState: StepFeatureState) -> Bool {
        let oldStateKs = StepFeatureStateKs(oldState)
        let newStateKs = StepFeatureStateKs(newState)

        let shouldNotify = oldStateKs != newStateKs

        if shouldNotify, case .data = oldStateKs, case .data = newStateKs {
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

    private func updateStepQuiz() {
        mainScheduler.schedule { [weak self] in
            guard let strongSelf = self,
                  case .data(let stepFeatureStateData) = strongSelf.stateKs else {
                return
            }

            strongSelf.stepQuizModuleInput?.update(
                isPracticingLoading: stepFeatureStateData.stepCompletionState.isPracticingLoading
            )
        }
    }

    // MARK: StepQuizUserPermissionRequest

    func handleSendDailyStudyRemindersPermissionRequestResult(isGranted: Bool) {
        let message = StepCompletionFeatureMessageRequestDailyStudyRemindersPermissionResult(
            isGranted: isGranted
        )

        if isGranted {
            Task(priority: .userInitiated) {
                let isNotificationPermissionGranted =
                  await notificationsRegistrationService.requestAuthorizationIfNeeded()

                await MainActor.run {
                    onNewMessage(StepFeatureMessageStepCompletionMessage(message: message))

                    if isNotificationPermissionGranted {
                        notificationService.scheduleDailyStudyReminderLocalNotifications(
                            analyticRoute: HyperskillAnalyticRoute.Learn.LearnStep(stepId: stepRoute.stepId)
                        )
                    }
                }
            }
        } else {
            onNewMessage(StepFeatureMessageStepCompletionMessage(message: message))
        }
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(StepFeatureMessageViewedEventMessage())
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
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageTopicCompletedModalShownEventMessage()
            )
        )
    }

    func topicCompletedModalViewControllerDidDisappear(_ viewController: TopicCompletedModalViewController) {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageTopicCompletedModalHiddenEventMessage()
            )
        )
    }
}

// MARK: - StepViewModel: ProblemOfDaySolvedModalViewControllerDelegate -

extension StepViewModel: ProblemOfDaySolvedModalViewControllerDelegate {
    func problemOfDaySolvedModalViewControllerDidTapGoToHomescreenButton(
        _ viewController: ProblemOfDaySolvedModalViewController
    ) {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageProblemOfDaySolvedModalGoBackClicked()
            )
        )

        viewController.dismiss(animated: true)
    }

    func problemOfDaySolvedModalViewControllerDidAppear(
        _ viewController: ProblemOfDaySolvedModalViewController
    ) {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageDailyStepCompletedModalShownEventMessage()
            )
        )
    }

    func problemOfDaySolvedModalViewControllerDidDisappear(
        _ viewController: ProblemOfDaySolvedModalViewController
    ) {
        onNewMessage(
            StepFeatureMessageStepCompletionMessage(
                message: StepCompletionFeatureMessageDailyStepCompletedModalHiddenEventMessage()
            )
        )
    }
}
