import shared
import SwiftUI

final class StepQuizViewModel: FeatureViewModel<
  StepQuizFeatureState,
  StepQuizFeatureMessage,
  StepQuizFeatureActionViewAction
> {
    let step: Step

    weak var childQuizModuleInput: StepQuizChildQuizInputProtocol?

    private let viewDataMapper: StepQuizViewDataMapper
    private let userPermissionRequestTextMapper: StepQuizUserPermissionRequestTextMapper

    private let notificationService: NotificationsService
    private let notificationsRegistrationService: NotificationsRegistrationService

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
        #warning("TODO: Implement quiz retry")
        logClickedRetryEvent()
        onNewMessage(StepQuizFeatureMessageCreateAttemptClicked(step: step, shouldResetReply: true))
    }

    func doQuizContinueAction() {
        onNewMessage(StepQuizFeatureMessageContinueClicked())
    }

    func makeViewData() -> StepQuizViewData {
        viewDataMapper.mapStepDataToViewData(step: step, state: state)
    }

    func makeUserPermissionRequestTitle(_ userPermissionRequest: StepQuizUserPermissionRequest) -> String {
        userPermissionRequestTextMapper.getTitle(request: userPermissionRequest)
    }

    func makeUserPermissionRequestMessage(_ userPermissionRequest: StepQuizUserPermissionRequest) -> String {
        userPermissionRequestTextMapper.getMessage(request: userPermissionRequest)
    }

    func handleDailyStudyRemindersPermissionRequestResult(isGranted: Bool) {
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
