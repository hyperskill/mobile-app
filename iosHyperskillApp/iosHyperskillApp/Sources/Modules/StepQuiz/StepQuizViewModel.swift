import Combine
import shared
import SwiftUI

final class StepQuizViewModel: FeatureViewModel<
  StepQuizFeatureState,
  StepQuizFeatureMessage,
  StepQuizFeatureActionViewAction
> {
    let step: Step

    weak var childQuizModuleInput: StepQuizChildQuizInputProtocol?
    private var updateChildQuizSubscription: AnyCancellable?

    private let viewDataMapper: StepQuizViewDataMapper
    private let userPermissionRequestTextMapper: StepQuizUserPermissionRequestTextMapper

    private let notificationService: NotificationsService
    private let notificationsRegistrationService: NotificationsRegistrationService

    var stateKs: StepQuizFeatureStateKs { .init(state) }

    init(
        step: Step,
        viewDataMapper: StepQuizViewDataMapper,
        userPermissionRequestTextMapper: StepQuizUserPermissionRequestTextMapper,
        notificationService: NotificationsService,
        notificationsRegistrationService: NotificationsRegistrationService,
        feature: Presentation_reduxFeature
    ) {
        self.step = step
        self.viewDataMapper = viewDataMapper
        self.userPermissionRequestTextMapper = userPermissionRequestTextMapper
        self.notificationService = notificationService
        self.notificationsRegistrationService = notificationsRegistrationService
        super.init(feature: feature)
    }

    override func shouldNotifyStateDidChange(oldState: StepQuizFeatureState, newState: StepQuizFeatureState) -> Bool {
        if oldState is StepQuizFeatureStateAttemptLoading && newState is StepQuizFeatureStateAttemptLoaded {
            updateChildQuizSubscription = objectWillChange.sink { [weak self] in
                guard let strongSelf = self else {
                    return
                }

                strongSelf.updateChildQuizSubscription?.cancel()
                strongSelf.updateChildQuizSubscription = nil

                strongSelf.updateChildQuiz()
            }
        }

        return StepQuizFeatureStateKs(oldState) != StepQuizFeatureStateKs(newState)
    }

    func loadAttempt(forceUpdate: Bool = false) {
        onNewMessage(StepQuizFeatureMessageInitWithStep(step: step, forceUpdate: forceUpdate))
    }

    func syncReply(_ reply: Reply) {
        onNewMessage(StepQuizFeatureMessageSyncReply(reply: reply))
    }

    func doMainQuizAction() {
        guard let reply = childQuizModuleInput?.createReply() else {
            return
        }

        onNewMessage(StepQuizFeatureMessageCreateSubmissionClicked(step: step, reply: reply))
    }

    func doQuizRetryAction() {
        logClickedRetryEvent()
        onNewMessage(StepQuizFeatureMessageCreateAttemptClicked(step: self.step, shouldResetReply: true))
    }

    func doQuizContinueAction() {
        onNewMessage(StepQuizFeatureMessageContinueClicked())
    }

    func doGoBackAction() {
        onNewMessage(StepQuizFeatureMessageProblemOfDaySolvedModalGoBackClicked())
    }

    func makeViewData() -> StepQuizViewData {
        viewDataMapper.mapStepDataToViewData(step: step, state: state)
    }

    private func updateChildQuiz() {
        mainScheduler.schedule { [weak self] in
            guard let strongSelf = self,
                  let attemptLoadedState = strongSelf.state as? StepQuizFeatureStateAttemptLoaded,
                  let dataset = attemptLoadedState.attempt.dataset else {
                return
            }

            let submissionStateEmpty = attemptLoadedState.submissionState as? StepQuizFeatureSubmissionStateEmpty
            let submissionStateLoaded = attemptLoadedState.submissionState as? StepQuizFeatureSubmissionStateLoaded

            let reply = submissionStateLoaded?.submission.reply ?? submissionStateEmpty?.reply

            strongSelf.childQuizModuleInput?.update(step: strongSelf.step, dataset: dataset, reply: reply)
        }
    }

    // MARK: StepQuizUserPermissionRequest

    func makeUserPermissionRequestTitle(_ userPermissionRequest: StepQuizUserPermissionRequest) -> String {
        userPermissionRequestTextMapper.getTitle(request: userPermissionRequest)
    }

    func makeUserPermissionRequestMessage(_ userPermissionRequest: StepQuizUserPermissionRequest) -> String {
        userPermissionRequestTextMapper.getMessage(request: userPermissionRequest)
    }

    func handleResetCodePermissionRequestResult(isGranted: Bool) {
        let message = StepQuizFeatureMessageRequestUserPermissionResult(
            userPermissionRequest: StepQuizUserPermissionRequest.resetCode,
            isGranted: isGranted
        )
        onNewMessage(message)
    }

    func handleSendDailyStudyRemindersPermissionRequestResult(isGranted: Bool) {
        let message = StepQuizFeatureMessageRequestUserPermissionResult(
            userPermissionRequest: StepQuizUserPermissionRequest.sendDailyStudyReminders,
            isGranted: isGranted
        )

        if isGranted {
            Task(priority: .userInitiated) {
                let isNotificationPermissionGranted =
                  await notificationsRegistrationService.requestAuthorizationIfNeeded()

                await MainActor.run {
                    onNewMessage(message)

                    if isNotificationPermissionGranted {
                        notificationService.scheduleDailyStudyReminderLocalNotifications(
                            analyticRoute: HyperskillAnalyticRoute.Learn.LearnStep(stepId: step.id)
                        )
                    }
                }
            }
        } else {
            onNewMessage(message)
        }
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(StepQuizFeatureMessageViewedEventMessage(stepId: step.id))
    }

    private func logClickedRetryEvent() {
        onNewMessage(StepQuizFeatureMessageClickedRetryEventMessage())
    }

    func logDailyStepCompletedModalShownEvent() {
        onNewMessage(StepQuizFeatureMessageDailyStepCompletedModalShownEventMessage())
    }

    func logDailyStepCompletedModalHiddenEvent() {
        onNewMessage(StepQuizFeatureMessageDailyStepCompletedModalHiddenEventMessage())
    }
}

// MARK: - StepQuizViewModel: StepQuizChildQuizDelegate -

extension StepQuizViewModel: StepQuizChildQuizOutputProtocol {
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
