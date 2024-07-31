import Combine
import shared
import SwiftUI

final class StepQuizViewModel: FeatureViewModel<
  StepQuizFeature.State,
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

    override func shouldNotifyStateDidChange(
        oldState: StepQuizFeature.State,
        newState: StepQuizFeature.State
    ) -> Bool {
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
        let reply =
            if let codeBlanksContentState = state.stepQuizCodeBlanksState as? StepQuizCodeBlanksFeatureStateContent {
                codeBlanksContentState.createReply()
            } else {
                childQuizModuleInput?.createReply()
            }

        guard let reply else {
            return assertionFailure("StepQuizViewModel: reply is nil")
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

    func doUnsupportedQuizSolveOnTheWebAction() {
        onNewMessage(StepQuizFeatureMessageUnsupportedQuizSolveOnTheWebClicked())
    }

    func doUnsupportedQuizGoToStudyPlanAction() {
        onNewMessage(StepQuizFeatureMessageUnsupportedQuizGoToStudyPlanClicked())
    }

    func makeViewData() -> StepQuizViewData {
        stepQuizViewDataMapper.mapStepDataToViewData(step: step, stepRoute: stepRoute, state: state)
    }

    private func updateChildQuiz() {
        mainScheduler.schedule { [weak self] in
            guard let strongSelf = self,
                  let attemptLoadedState = strongSelf.state.stepQuizState as? StepQuizFeatureStepQuizStateAttemptLoaded,
                  let dataset = attemptLoadedState.attempt.dataset else {
                return
            }

            let reply = StepQuizStateExtensionsKt.reply(attemptLoadedState.submissionState)

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

    // MARK: StepQuizToolbar

    func doLimitsToolbarAction() {
        onNewMessage(
            StepQuizFeatureMessageStepQuizToolbarMessage(
                message: StepQuizToolbarFeatureMessageProblemsLimitClicked()
            )
        )
    }

    func doTheoryToolbarAction() {
        onNewMessage(StepQuizFeatureMessageTheoryToolbarItemClicked())
    }

    // MARK: StepQuizHints

    func handleStepQuizHints(message: StepQuizHintsFeatureMessage) {
        onNewMessage(StepQuizFeatureMessageStepQuizHintsMessage(message: message))
    }

    // MARK: Analytic

    private func logClickedRetryEvent() {
        onNewMessage(StepQuizFeatureMessageClickedRetryEventMessage())
    }

    func logClickedStepTextDetailsEvent() {
        onNewMessage(StepQuizFeatureMessageClickedStepTextDetailsEventMessage())
    }
}

// MARK: - StepQuizViewModel: StepQuizChildQuizOutputProtocol -

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

// MARK: - StepQuizViewModel: StepQuizCodeBlanksOutputProtocol -

extension StepQuizViewModel: StepQuizCodeBlanksOutputProtocol {
    func handleStepQuizCodeBlanksDidTapOnSuggestion(_ suggestion: Suggestion) {
        onNewMessage(
            StepQuizFeatureMessageStepQuizCodeBlanksMessage(
                message: StepQuizCodeBlanksFeatureMessageSuggestionClicked(suggestion: suggestion)
            )
        )
    }

    func handleStepQuizCodeBlanksDidTapOnCodeBlock(_ codeBlock: StepQuizCodeBlanksViewStateCodeBlockItem) {
        onNewMessage(
            StepQuizFeatureMessageStepQuizCodeBlanksMessage(
                message: StepQuizCodeBlanksFeatureMessageCodeBlockClicked(codeBlockItem: codeBlock)
            )
        )
    }

    func handleStepQuizCodeBlanksDidTapDelete() {
        onNewMessage(
            StepQuizFeatureMessageStepQuizCodeBlanksMessage(
                message: StepQuizCodeBlanksFeatureMessageDeleteButtonClicked()
            )
        )
    }

    func handleStepQuizCodeBlanksDidTapEnter() {
        onNewMessage(
            StepQuizFeatureMessageStepQuizCodeBlanksMessage(
                message: StepQuizCodeBlanksFeatureMessageEnterButtonClicked()
            )
        )
    }
}

// MARK: - StepQuizViewModel: StepQuizProblemOnboardingModalViewControllerDelegate -

extension StepQuizViewModel: StepQuizProblemOnboardingModalViewControllerDelegate {
    func stepQuizProblemOnboardingModalViewControllerDidAppear(
        _ viewController: StepQuizProblemOnboardingModalViewController,
        modalType: StepQuizFeatureProblemOnboardingModalKs
    ) {
        onNewMessage(StepQuizFeatureMessageProblemOnboardingModalShownMessage(modalType: modalType.sealed))
    }

    func stepQuizProblemOnboardingModalViewControllerDidDisappear(
        _ viewController: StepQuizProblemOnboardingModalViewController,
        modalType: StepQuizFeatureProblemOnboardingModalKs
    ) {
        onNewMessage(StepQuizFeatureMessageProblemOnboardingModalHiddenMessage(modalType: modalType.sealed))
    }
}
