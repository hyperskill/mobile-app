import shared
import SwiftUI
import UIKit

extension StepQuizView {
    struct Appearance {
        let interItemSpacing = LayoutInsets.defaultInset
    }
}

struct StepQuizView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: StepQuizViewModel

    @EnvironmentObject private var modalRouter: SwiftUIModalRouter
    @EnvironmentObject private var stackRouter: SwiftUIStackRouter
    @EnvironmentObject private var panModalPresenter: PanModalPresenter

    @State private var fillBlanksSelectOptionsViewHeight: CGFloat = 0
    private var shouldRenderFillBlanksSpacer: Bool {
        if #available(iOS 15.0, *) {
            return false
        } else {
            return true
        }
    }

    var body: some View {
        buildBody()
            .navigationBarTitleDisplayMode(.inline)
            .onAppear {
                viewModel.startListening()
                viewModel.onViewAction = handleViewAction(_:)

                viewModel.doProvideModuleInput()
            }
            .onDisappear {
                viewModel.stopListening()
                viewModel.onViewAction = nil
            }
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        if viewModel.stepQuizStateKs == .networkError {
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: .clear,
                    action: viewModel.doRetryLoadAttempt
                )
            )
        } else {
            let viewData = viewModel.makeViewData()

            ScrollView {
                VStack(alignment: .leading, spacing: appearance.interItemSpacing) {
                    if case .unsupported = viewData.quizType {
                        StepQuizUnsupportedView(
                            onSolveButtonTap: viewModel.doUnsupportedQuizSolveOnTheWebAction,
                            onGoToStudyPlanButtonTap: viewModel.doUnsupportedQuizGoToStudyPlanAction
                        )
                    } else {
                        StepExpandableStepTextView(
                            text: viewData.stepText,
                            isExpanded: true,
                            onExpandButtonTap: viewModel.logClickedStepTextDetailsEvent
                        )

                        if StepQuizHintsFeature.shared.isHintsFeatureAvailable(step: viewModel.step) {
                            StepQuizHintsView(
                                state: viewModel.state.stepQuizHintsState,
                                onNewMessage: { [weak viewModel] message in
                                    viewModel?.handleStepQuizHints(message: message)
                                }
                            )
                            .equatable()
                        }

                        buildQuizContent(
                            state: viewModel.state,
                            step: viewModel.step,
                            quizName: viewData.quizName,
                            quizType: viewData.quizType,
                            formattedStats: viewData.formattedStats,
                            feedbackHintText: viewData.feedbackHintText
                        )
                    }
                }
                .padding()
                .introspectScrollView { scrollView in
                    scrollView.shouldIgnoreScrollingAdjustment = true
                }

                if shouldRenderFillBlanksSpacer {
                    Spacer(minLength: fillBlanksSelectOptionsViewHeight)
                }
            }
            .safeAreaInsetBottomCompatibility(
                buildFillBlanksSelectOptionsView(
                    quizType: viewData.quizType,
                    attemptLoadedState: StepQuizStateExtentionsKt.attemptLoadedState(viewModel.state.stepQuizState)
                )
            )
            .if(StepQuizResolver.shared.isTheoryToolbarItemAvailable(state: viewModel.state.stepQuizState)) {
                $0.toolbar {
                    // buildIf is only available in iOS 16.0 or newer
                    ToolbarItem(placement: .navigationBarTrailing) {
                        Button(
                            Strings.Step.theory,
                            action: viewModel.doTheoryToolbarAction
                        )
                        .disabled(StepQuizResolver.shared.isQuizLoading(state: viewModel.state.stepQuizState))
                    }
                }
            }
        }
    }

    // swiftlint:disable function_parameter_count
    @ViewBuilder
    private func buildQuizContent(
        state: StepQuizFeature.State,
        step: Step,
        quizName: String?,
        quizType: StepQuizChildQuizType,
        formattedStats: String?,
        feedbackHintText: String?
    ) -> some View {
        if let quizName {
            StepQuizNameView(text: quizName)
                .padding(.top)
        }

        if let attemptLoadedState = StepQuizStateExtentionsKt.attemptLoadedState(state.stepQuizState) {
            buildChildQuiz(quizType: quizType, step: step, attemptLoadedState: attemptLoadedState)

            if let formattedStats {
                StepQuizStatsView(text: formattedStats)
            }

            buildQuizStatusView(state: state.stepQuizState, attemptLoadedState: attemptLoadedState)

            if let feedbackHintText {
                StepQuizFeedbackView(text: feedbackHintText)
            }

            buildQuizActionButtons(quizType: quizType, state: state, attemptLoadedState: attemptLoadedState)
        } else {
            StepQuizSkeletonViewFactory.makeSkeleton(for: quizType)
        }
    }
    // swiftlint:enable function_parameter_count

    @ViewBuilder
    private func buildChildQuiz(
        quizType: StepQuizChildQuizType,
        step: Step,
        attemptLoadedState: StepQuizFeatureStepQuizStateAttemptLoaded
    ) -> some View {
        if let dataset = attemptLoadedState.attempt.dataset {
            let reply = StepQuizStateExtensionsKt.reply(attemptLoadedState.submissionState)

            StepQuizChildQuizViewFactory.make(
                quizType: quizType,
                step: step,
                dataset: dataset,
                reply: reply,
                provideModuleInputCallback: { viewModel.childQuizModuleInput = $0 },
                moduleOutput: viewModel
            )
            .disabled(!StepQuizResolver.shared.isQuizEnabled(state: attemptLoadedState))
            .environment(\.isFixCodeMistakesBadgeVisible, attemptLoadedState.isFixGptCodeGenerationMistakesBadgeVisible)
        }
    }

    @ViewBuilder
    private func buildQuizStatusView(
        state: StepQuizFeatureStepQuizState,
        attemptLoadedState: StepQuizFeatureStepQuizStateAttemptLoaded
    ) -> some View {
        if state is StepQuizFeatureStepQuizStateAttemptLoading {
            StepQuizStatusView(state: .loading)
        } else if let submissionState = attemptLoadedState.submissionState as? StepQuizFeatureSubmissionStateLoaded {
            if let replyValidationError = submissionState.replyValidation as? ReplyValidationResultError {
                StepQuizStatusView(state: .invalidReply(message: replyValidationError.message))
            } else if let submissionStatus = submissionState.submission.status {
                StepQuizStatusView.build(submissionStatus: submissionStatus)
            }
        }
    }

    @ViewBuilder
    private func buildQuizActionButtons(
        quizType: StepQuizChildQuizType,
        state: StepQuizFeature.State,
        attemptLoadedState: StepQuizFeatureStepQuizStateAttemptLoaded
    ) -> some View {
        let submissionStatus: SubmissionStatus? = {
            if let submissionStateLoaded = attemptLoadedState.submissionState as? StepQuizFeatureSubmissionStateLoaded {
                return submissionStateLoaded.submission.status
            }
            return SubmissionStatus.local
        }()

        if submissionStatus == SubmissionStatus.wrong || submissionStatus == SubmissionStatus.rejected {
            if quizType.isCodeRelated {
                StepQuizActionButtons.retryLogoAndRunSolution(
                    retryButtonAction: viewModel.doQuizRetryAction,
                    runSolutionButtonState: .init(submissionStatus: submissionStatus),
                    runSolutionButtonAction: viewModel.doMainQuizAction
                )
                .disabled(StepQuizResolver.shared.isQuizLoading(state: state.stepQuizState))
            } else if StepQuizResolver.shared.isNeedRecreateAttemptForNewSubmission(step: viewModel.step) {
                StepQuizActionButtons.retry(action: viewModel.doQuizRetryAction)
                    .disabled(StepQuizResolver.shared.isQuizLoading(state: state.stepQuizState))
            } else {
                StepQuizActionButtons.submit(
                    state: .init(submissionStatus: submissionStatus),
                    action: viewModel.doMainQuizAction
                )
                .disabled(!StepQuizResolver.shared.isQuizEnabled(state: attemptLoadedState))
            }
        } else if submissionStatus == SubmissionStatus.correct {
            if StepQuizResolver.shared.isQuizRetriable(step: viewModel.step) {
                StepQuizActionButtons.retryLogoAndContinue(
                    retryButtonAction: viewModel.doQuizRetryAction,
                    continueButton: .init(
                        isLoading: viewModel.isPracticingLoading,
                        action: viewModel.doQuizContinueAction
                    )
                )
                .disabled(
                    StepQuizResolver.shared.isQuizLoading(state: state.stepQuizState) ||
                    viewModel.isPracticingLoading
                )
            } else {
                StepQuizActionButtons.continue(
                    isLoading: viewModel.isPracticingLoading,
                    action: viewModel.doQuizContinueAction
                )
                .disabled(StepQuizResolver.shared.isQuizLoading(state: state.stepQuizState))
            }
        } else {
            if quizType.isCodeRelated {
                StepQuizActionButtons.runSolution(
                    state: .init(submissionStatus: submissionStatus),
                    action: viewModel.doMainQuizAction
                )
                .disabled(!StepQuizResolver.shared.isQuizEnabled(state: attemptLoadedState))
            } else {
                StepQuizActionButtons.submit(
                    state: .init(submissionStatus: submissionStatus),
                    action: viewModel.doMainQuizAction
                )
                .disabled(!StepQuizResolver.shared.isQuizEnabled(state: attemptLoadedState))
            }
        }
    }

    @ViewBuilder
    private func buildFillBlanksSelectOptionsView(
        quizType: StepQuizChildQuizType,
        attemptLoadedState: StepQuizFeatureStepQuizStateAttemptLoaded?
    ) -> some View {
        if case .fillBlanks(let mode) = quizType, mode == .select,
           let attemptLoadedState {
            StepQuizFillBlanksSelectOptionsViewWrapper(
                moduleOutput: viewModel.childQuizModuleInput as? StepQuizFillBlanksSelectOptionsOutputProtocol,
                moduleInput: { [weak viewModel] moduleInput in
                    guard let viewModel else {
                        return
                    }

                    viewModel.fillBlanksSelectOptionsModuleInput = moduleInput
                },
                isUserInteractionEnabled: StepQuizResolver.shared.isQuizEnabled(state: attemptLoadedState),
                onNewHeight: { height in
                    if fillBlanksSelectOptionsViewHeight != height {
                        DispatchQueue.main.async {
                            fillBlanksSelectOptionsViewHeight = height
                        }
                    }
                }
            )
            .background(TransparentBlurView())
            .edgesIgnoringSafeArea(.all)
            .frame(height: fillBlanksSelectOptionsViewHeight)
            .disabled(!StepQuizResolver.shared.isQuizEnabled(state: attemptLoadedState))
        }
    }

    private func handleViewAction(_ viewAction: StepQuizFeatureActionViewAction) {
        switch StepQuizFeatureActionViewActionKs(viewAction) {
        case .showNetworkError:
            ProgressHUD.showError(status: Strings.Common.connectionError)
        case .requestResetCode:
            presentResetCodePermissionAlert()
        case .showProblemsLimitReachedModal(let showProblemsLimitReachedModalViewAction):
            presentProblemsLimitReachedModal(
                params: ProblemsLimitInfoModalFeatureParams(
                    subscription: showProblemsLimitReachedModalViewAction.subscription,
                    chargeLimitsStrategy: showProblemsLimitReachedModalViewAction.chargeLimitsStrategy,
                    context: showProblemsLimitReachedModalViewAction.context,
                    stepRoute: showProblemsLimitReachedModalViewAction.stepRoute
                )
            )
        case .showProblemOnboardingModal(let showProblemOnboardingModalViewAction):
            presentProblemOnboardingModal(modalType: showProblemOnboardingModalViewAction.modalType)
        case .navigateTo(let viewActionNavigateTo):
            switch StepQuizFeatureActionViewActionNavigateToKs(viewActionNavigateTo) {
            case .stepScreen(let navigateToStepScreenViewAction):
                let assembly = StepAssembly(stepRoute: navigateToStepScreenViewAction.stepRoute)
                stackRouter.pushViewController(assembly.makeModule())
            case .studyPlan:
                stackRouter.popViewController(
                    animated: true,
                    completion: {
                        TabBarRouter(tab: .studyPlan).route()
                    }
                )
            }
        case .stepQuizHintsViewAction(let stepQuizHintsViewAction):
            switch StepQuizHintsFeatureActionViewActionKs(stepQuizHintsViewAction.viewAction) {
            case .showNetworkError:
                ProgressHUD.showError(status: Strings.Common.connectionError)
            }
        case .createMagicLinkState(let createMagicLinkStateViewAction):
            switch StepQuizFeatureActionViewActionCreateMagicLinkStateKs(createMagicLinkStateViewAction) {
            case .error:
                ProgressHUD.showError()
            case .loading:
                ProgressHUD.show()
            case .success:
                ProgressHUD.showSuccess()
            }
        case .openUrl(let data):
            WebControllerManager.shared.presentWebControllerWithURLString(data.url, controllerType: .inAppSafari)
        case .hideProblemsLimitReachedModal:
            panModalPresenter.dismissPanModal(
                condition: { ($0 as? ProblemsLimitInfoModalViewController) != nil }
            )
        case .problemsLimitViewAction(let problemsLimitViewAction):
            handleStepQuizToolbarViewAction(viewAction: problemsLimitViewAction.viewAction)
        }
    }
    
    private func handleStepQuizToolbarViewAction(viewAction: ProblemsLimitFeatureActionViewAction) {
        // no op
    }
}

// MARK: - StepQuizView (Modals) -

private extension StepQuizView {
    func presentResetCodePermissionAlert() {
        let alert = UIAlertController(
            title: Strings.StepQuiz.ResetCodeAlert.title,
            message: Strings.StepQuiz.ResetCodeAlert.text,
            preferredStyle: .alert
        )
        alert.addAction(
            UIAlertAction(
                title: Strings.Common.cancel,
                style: .cancel,
                handler: { [weak viewModel] _ in
                    viewModel?.handleResetCodePermissionRequestResult(isGranted: false)
                }
            )
        )
        alert.addAction(
            UIAlertAction(
                title: Strings.StepQuizCode.reset,
                style: .destructive,
                handler: { [weak viewModel] _ in
                    viewModel?.handleResetCodePermissionRequestResult(isGranted: true)
                }
            )
        )

        modalRouter.presentAlert(alert)
    }

    func presentProblemsLimitReachedModal(params: ProblemsLimitInfoModalFeatureParams) {
        let assembly = ProblemsLimitInfoModalAssembly(params: params)
        panModalPresenter.presentIfPanModal(assembly.makeModule())
    }

    func presentProblemOnboardingModal(modalType: StepQuizFeatureProblemOnboardingModal) {
        let panModal = StepQuizProblemOnboardingModalViewController(
            modalType: StepQuizFeatureProblemOnboardingModalKs(modalType),
            delegate: viewModel
        )
        panModalPresenter.presentPanModal(panModal)
    }
}
