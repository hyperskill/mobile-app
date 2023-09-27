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
    var stepQuizStateKs: StepQuizFeatureStepQuizStateKs { .init(state.stepQuizState) }

    @Published var isPracticingLoading = false

    init(
        step: Step,
        stepRoute: StepRoute,
        moduleOutput: StepQuizOutputProtocol?,
        provideModuleInputCallback: @escaping (StepQuizInputProtocol?) -> Void,
        viewDataMapper: StepQuizViewDataMapper,
        feature: Presentation_reduxFeature
    ) {
        self.step = step
        self.stepRoute = stepRoute
        self.moduleOutput = moduleOutput
        self.provideModuleInputCallback = provideModuleInputCallback
        self.stepQuizViewDataMapper = viewDataMapper

        super.init(feature: feature)

        onNewMessage(StepQuizFeatureMessageInitWithStep(step: step, forceUpdate: false))
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

    func doRetryLoadAttempt() {
        onNewMessage(StepQuizFeatureMessageInitWithStep(step: step, forceUpdate: true))
    }

    func syncReply(_ reply: Reply) {
        #warning("TODO: refactor state menagment")
        let shouldSyncReply = StepQuizResolver.shared.shouldSyncReply(state: state.stepQuizState)

        #if DEBUG
        print("StepQuizViewModel: shouldSyncReply = \(shouldSyncReply)")
        #endif

        if shouldSyncReply {
            onNewMessage(StepQuizFeatureMessageSyncReply(reply: reply))
        }
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

    // MARK: StepQuizUserResetCodeRequest

    func handleResetCodePermissionRequestResult(isGranted: Bool) {
        onNewMessage(
            StepQuizFeatureMessageRequestResetCodeResult(
                isGranted: isGranted
            )
        )
    }

    func doTheoryToolbarAction() {
        onNewMessage(StepQuizFeatureMessageTheoryToolbarItemClicked())
    }

    // MARK: Analytic

    private func logClickedRetryEvent() {
        onNewMessage(StepQuizFeatureMessageClickedRetryEventMessage())
    }

    func logClickedStepTextDetailsEvent() {
        onNewMessage(StepQuizFeatureMessageClickedStepTextDetailsEventMessage())
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

// MARK: - StepQuizViewModel: StepQuizParsonsOnboardingModalViewControllerDelegate -

extension StepQuizViewModel: StepQuizParsonsOnboardingModalViewControllerDelegate {
    func stepQuizParsonsOnboardingModalViewControllerDidAppear(
        _ viewController: StepQuizParsonsOnboardingModalViewController
    ) {
        onNewMessage(StepQuizFeatureMessageParsonsProblemOnboardingModalShownMessage())
    }

    func stepQuizParsonsOnboardingModalViewControllerDidDisappear(
        _ viewController: StepQuizParsonsOnboardingModalViewController
    ) {
        onNewMessage(StepQuizFeatureMessageParsonsProblemOnboardingModalHiddenEventMessage())
    }
}
