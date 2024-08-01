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

    @State private var scrollPosition: ScrollPosition?

    var body: some View {
        let viewData = viewModel.makeViewData()

        buildBody(viewData: viewData)
            .animation(.default, value: viewModel.state)
            .onAppear {
                viewModel.onViewAction = handleViewAction(_:)
                viewModel.startListening()

                viewModel.doProvideModuleInput()
            }
            .onDisappear {
                viewModel.onViewAction = nil
                viewModel.stopListening()
            }
            .if(viewData.navigationTitle != nil) {
                $0.navigationTitle(viewData.navigationTitle.require())
            }
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody(viewData: StepQuizViewData) -> some View {
        if viewModel.stepQuizStateKs == .networkError {
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: .clear,
                    action: viewModel.doRetryLoadAttempt
                )
            )
        } else {
            ScrollViewReader { scrollViewProxy in
                ScrollView {
                    VStack(alignment: .leading, spacing: appearance.interItemSpacing) {
                        if case .unsupported = viewData.quizType {
                            StepQuizUnsupportedView(
                                onSolveButtonTap: viewModel.doUnsupportedQuizSolveOnTheWebAction,
                                onGoToStudyPlanButtonTap: viewModel.doUnsupportedQuizGoToStudyPlanAction
                            )
                        } else {
                            if viewModel.stepRoute is StepRouteStageImplement {
                                LatexView(
                                    text: viewData.stepText.require(),
                                    configuration: .stepText()
                                )
                            } else {
                                StepExpandableStepTextView(
                                    title: viewData.stepTextHeaderTitle.require(),
                                    text: viewData.stepText.require(),
                                    isExpanded: true,
                                    onExpandButtonTap: viewModel.logClickedStepTextDetailsEvent
                                )
                            }

                            if StepQuizHintsFeature.shared.isHintsFeatureAvailable(step: viewModel.step) {
                                StepQuizHintsView(
                                    state: viewModel.state.stepQuizHintsState,
                                    onNewMessage: { [weak viewModel] message in
                                        viewModel?.handleStepQuizHints(message: message)
                                    }
                                )
                                .equatable()
                                .id(ScrollPosition.hints)
                            }

                            buildQuizContent(
                                state: viewModel.state,
                                step: viewModel.step,
                                quizName: viewData.quizName,
                                quizType: viewData.quizType,
                                formattedStats: viewData.formattedStats,
                                stepQuizFeedbackState: viewData.stepQuizFeedbackState.require()
                            )
                        }
                    }
                    .padding()
                }
                .stepQuizToolbar(
                    state: viewModel.state,
                    stepRoute: viewModel.stepRoute,
                    onLimitsButtonTap: viewModel.doLimitsToolbarAction,
                    onTheoryButtonTap: viewModel.doTheoryToolbarAction
                )
                .onChange(of: scrollPosition) { newScrollPosition in
                    guard let newScrollPosition else {
                        return
                    }

                    withAnimation {
                        scrollViewProxy.scrollTo(newScrollPosition, anchor: .bottom)
                    }

                    scrollPosition = nil
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
        stepQuizFeedbackState: StepQuizFeedbackStateKs
    ) -> some View {
        if let quizName {
            StepQuizNameView(text: quizName)
                .padding(.top)
        }

        if let attemptLoadedState = StepQuizStateExtensionsKt.attemptLoadedState(state.stepQuizState) {
            buildChildQuiz(quizType: quizType, step: step, attemptLoadedState: attemptLoadedState)

            if let formattedStats {
                StepQuizStatsView(text: formattedStats)
            }

            if state.stepQuizState is StepQuizFeatureStepQuizStateAttemptLoading {
                StepQuizFeedbackStatusView(state: .loading)
            } else {
                StepQuizFeedbackView(
                    stepQuizFeedbackState: stepQuizFeedbackState,
                    onAction: viewModel.doFeedbackAction(actionType:)
                )
            }

            buildQuizActionButtons(quizType: quizType, state: state, attemptLoadedState: attemptLoadedState)
                .id(ScrollPosition.callToActionButton)
        } else {
            StepQuizSkeletonViewFactory.makeSkeleton(for: quizType)
                .padding(.top)
        }
    }
    // swiftlint:enable function_parameter_count

    @ViewBuilder
    private func buildChildQuiz(
        quizType: StepQuizChildQuizType,
        step: Step,
        attemptLoadedState: StepQuizFeatureStepQuizStateAttemptLoaded
    ) -> some View {
        let stepQuizCodeBlanksState = viewModel.state.stepQuizCodeBlanksState
        if stepQuizCodeBlanksState is StepQuizCodeBlanksFeatureStateContent {
            StepQuizCodeBlanksAssembly(
                state: stepQuizCodeBlanksState,
                moduleOutput: viewModel
            )
            .makeModule()
            .equatable()
            .disabled(!StepQuizResolver.shared.isQuizEnabled(state: attemptLoadedState))
        } else if let dataset = attemptLoadedState.attempt.dataset {
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

    // MARK: - Inner Types -

    enum ScrollPosition: Hashable {
        case hints
        case callToActionButton
    }
}

// MARK: - StepQuizView (ViewAction) -

private extension StepQuizView {
    func handleViewAction(_ viewAction: StepQuizFeatureActionViewAction) {
        switch StepQuizFeatureActionViewActionKs(viewAction) {
        case .showNetworkError:
            ProgressHUD.showError(status: Strings.Common.connectionError)
        case .requestResetCode:
            presentResetCodePermissionAlert()
        case .showProblemsLimitReachedModal(let data):
            presentProblemsLimitInfoModal(
                params: ProblemsLimitInfoModalFeatureParams(
                    subscription: data.subscription,
                    chargeLimitsStrategy: data.chargeLimitsStrategy,
                    context: data.context
                )
            )
        case .showProblemOnboardingModal(let data):
            presentProblemOnboardingModal(modalType: data.modalType)
        case .hideProblemsLimitReachedModal:
            panModalPresenter.dismissPanModal(
                condition: { ($0 as? ProblemsLimitInfoModalViewController) != nil }
            )
        case .createMagicLinkState(let createMagicLinkStateViewAction):
            handleCreateMagicLinkStateViewAction(createMagicLinkStateViewAction)
        case .openUrl(let data):
            WebControllerManager.shared.presentWebControllerWithURLString(
                data.url,
                controllerType: .inAppSafari
            )
        case .navigateTo(let navigateToViewAction):
            handleNavigateToViewAction(navigateToViewAction)
        case .stepQuizHintsViewAction(let stepQuizHintsViewAction):
            StepQuizHintsViewActionHandler.handle(viewAction: stepQuizHintsViewAction.viewAction)
        case .stepQuizToolbarViewAction(let stepQuizToolbarViewAction):
            handleStepQuizToolbarViewAction(viewAction: stepQuizToolbarViewAction.viewAction)
        case .hapticFeedback(let hapticFeedbackViewAction):
            handleHapticFeedbackViewAction(hapticFeedbackViewAction)
        case .scrollTo(let scrollToViewAction):
            handleScrollToViewAction(scrollToViewAction)
        case .requestShowComments:
            viewModel.doRequestShowComments()
        case .requestSkipStep:
            viewModel.doRequestSkipStep()
        case .stepQuizCodeBlanksViewAction(let stepQuizCodeBlanksViewAction):
            assertionFailure(
                "StepQuizView :: did receive unexpected StepQuizCodeBlanksViewAction: \(stepQuizCodeBlanksViewAction)"
            )
        }
    }

    func handleCreateMagicLinkStateViewAction(_ viewAction: StepQuizFeatureActionViewActionCreateMagicLinkState) {
        switch StepQuizFeatureActionViewActionCreateMagicLinkStateKs(viewAction) {
        case .error:
            ProgressHUD.showError()
        case .loading:
            ProgressHUD.show()
        case .success:
            ProgressHUD.showSuccess()
        }
    }

    func handleNavigateToViewAction(_ viewAction: StepQuizFeatureActionViewActionNavigateTo) {
        switch StepQuizFeatureActionViewActionNavigateToKs(viewAction) {
        case .theoryStepScreen(let data):
            let assembly = StepAssembly(stepRoute: data.stepRoute)
            stackRouter.pushViewController(assembly.makeModule())
        case .studyPlan:
            stackRouter.popViewController(
                animated: true,
                completion: {
                    TabBarRouter(tab: .studyPlan).route()
                }
            )
        }
    }

    func handleStepQuizToolbarViewAction(viewAction: StepQuizToolbarFeatureActionViewAction) {
        switch StepQuizToolbarFeatureActionViewActionKs(viewAction) {
        case .showProblemsLimitInfoModal(let data):
            presentProblemsLimitInfoModal(
                params: ProblemsLimitInfoModalFeatureParams(
                    subscription: data.subscription,
                    chargeLimitsStrategy: data.chargeLimitsStrategy,
                    context: data.context
                )
            )
        }
    }

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

    func presentProblemsLimitInfoModal(params: ProblemsLimitInfoModalFeatureParams) {
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

    func handleHapticFeedbackViewAction(_ viewAction: StepQuizFeatureActionViewActionHapticFeedback) {
        let feedbackType: FeedbackGenerator.FeedbackType =
            switch StepQuizFeatureActionViewActionHapticFeedbackKs(viewAction) {
            case .replyValidationError:
                .notification(.warning)
            case .correctSubmission:
                .notification(.success)
            case .wrongSubmission:
                .notification(.error)
            }
        FeedbackGenerator(feedbackType: feedbackType).triggerFeedback()
    }

    func handleScrollToViewAction(_ viewAction: StepQuizFeatureActionViewActionScrollTo) {
        let targetScrollPosition =
            switch StepQuizFeatureActionViewActionScrollToKs(viewAction) {
            case .hints:
                ScrollPosition.hints
            case .callToActionButton:
                ScrollPosition.callToActionButton
            }

        DispatchQueue.main.asyncAfter(deadline: .now() + 0.33) {
            self.scrollPosition = targetScrollPosition
        }
    }
}
