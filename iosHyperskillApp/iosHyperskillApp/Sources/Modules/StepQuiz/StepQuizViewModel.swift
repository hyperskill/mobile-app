import Combine
import shared
import SwiftUI

final class StepQuizViewModel: FeatureViewModel<
  StepQuizFeatureState,
  StepQuizFeatureMessage,
  StepQuizFeatureActionViewAction
> {
    let step: Step
    let stepRoute: StepRoute

    weak var moduleOutput: StepQuizOutputProtocol?
    private let provideModuleInputCallback: (StepQuizInputProtocol?) -> Void

    weak var childQuizModuleInput: StepQuizChildQuizInputProtocol?
    private var updateChildQuizSubscription: AnyCancellable?

    private let stepQuizViewDataMapper: StepQuizViewDataMapper
    private let userPermissionRequestTextMapper: StepQuizUserPermissionRequestTextMapper
    private let problemsLimitViewStateMapper: ProblemsLimitViewStateMapper

    private let notificationService: NotificationsService
    private let notificationsRegistrationService: NotificationsRegistrationService

    var stepQuizStateKs: StepQuizFeatureStepQuizStateKs { .init(state.stepQuizState) }
    var problemsLimitViewStateKs: ProblemsLimitFeatureViewStateKs {
        .init(problemsLimitViewStateMapper.mapState(state: state.problemsLimitState))
    }

    @Published var isPracticingLoading = false

    init(
        step: Step,
        stepRoute: StepRoute,
        moduleOutput: StepQuizOutputProtocol?,
        provideModuleInputCallback: @escaping (StepQuizInputProtocol?) -> Void,
        viewDataMapper: StepQuizViewDataMapper,
        userPermissionRequestTextMapper: StepQuizUserPermissionRequestTextMapper,
        problemsLimitViewStateMapper: ProblemsLimitViewStateMapper,
        notificationService: NotificationsService,
        notificationsRegistrationService: NotificationsRegistrationService,
        feature: Presentation_reduxFeature
    ) {
        self.step = step
        self.stepRoute = stepRoute
        self.moduleOutput = moduleOutput
        self.provideModuleInputCallback = provideModuleInputCallback
        self.stepQuizViewDataMapper = viewDataMapper
        self.userPermissionRequestTextMapper = userPermissionRequestTextMapper
        self.problemsLimitViewStateMapper = problemsLimitViewStateMapper
        self.notificationService = notificationService
        self.notificationsRegistrationService = notificationsRegistrationService
        super.init(feature: feature)
    }

    override func shouldNotifyStateDidChange(oldState: StepQuizFeatureState, newState: StepQuizFeatureState) -> Bool {
        if oldState.stepQuizState is StepQuizFeatureStepQuizStateAttemptLoading
            && newState.stepQuizState is StepQuizFeatureStepQuizStateAttemptLoaded {
            updateChildQuizSubscription = objectWillChange.sink { [weak self] in
                guard let strongSelf = self else {
                    return
                }

                strongSelf.updateChildQuizSubscription?.cancel()
                strongSelf.updateChildQuizSubscription = nil

                strongSelf.updateChildQuiz()
            }
        }

        return !oldState.isEqual(newState)
    }

    func doProvideModuleInput() {
        provideModuleInputCallback(self)
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
        moduleOutput?.stepQuizDidRequestContinue()
    }

    func doGoBackAction() {
        onNewMessage(StepQuizFeatureMessageProblemOfDaySolvedModalGoBackClicked())
    }

    func makeViewData() -> StepQuizViewData {
        stepQuizViewDataMapper.mapStepDataToViewData(step: step, state: stepQuizStateKs)
    }

    private func updateChildQuiz() {
        mainScheduler.schedule { [weak self] in
            guard let strongSelf = self,
                  let attemptLoadedState = strongSelf.state.stepQuizState as? StepQuizFeatureStepQuizStateAttemptLoaded,
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

    func doReloadProblemsLimit() {
        onNewMessage(
            StepQuizFeatureMessageProblemsLimitMessage(
                message: ProblemsLimitFeatureMessageInitialize(forceUpdate: true)
            )
        )
    }

    // MARK: Analytic

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

// MARK: - StepQuizViewModel: StepQuizInputProtocol -

extension StepQuizViewModel: StepQuizInputProtocol {
    func update(isPracticingLoading: Bool) {
        self.isPracticingLoading = isPracticingLoading
    }
}

// MARK: - StepQuizViewModel: ProblemsLimitReachedModalViewControllerDelegate -

extension StepQuizViewModel: ProblemsLimitReachedModalViewControllerDelegate {
    func problemsLimitReachedModalViewControllerDidTapGoToHomescreenButton(
        _ viewController: ProblemsLimitReachedModalViewController
    ) {
        onNewMessage(StepQuizFeatureMessageProblemsLimitReachedModalGoToHomeScreenClicked())
        viewController.dismiss(animated: true)
    }

    func problemsLimitReachedModalViewControllerDidAppear(_ viewController: ProblemsLimitReachedModalViewController) {
        onNewMessage(StepQuizFeatureMessageProblemsLimitReachedModalShownEventMessage())
    }

    func problemsLimitReachedModalViewControllerDidDisappear(
        _ viewController: ProblemsLimitReachedModalViewController
    ) {
        onNewMessage(StepQuizFeatureMessageProblemsLimitReachedModalHiddenEventMessage())
    }
}
