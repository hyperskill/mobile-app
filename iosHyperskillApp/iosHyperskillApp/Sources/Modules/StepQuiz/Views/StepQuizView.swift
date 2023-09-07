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
                        StepQuizStatusView(state: .unsupportedQuiz)

                        StepTextView(text: viewData.stepText)
                    } else {
                        StepTextView(text: viewData.stepText)

                        if viewData.stepHasHints {
                            StepQuizHintsAssembly(
                                stepID: viewModel.step.id,
                                stepRoute: viewModel.stepRoute
                            ).makeModule()
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
            }
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

    @ViewBuilder
    private func buildQuizContent(
        state: StepQuizFeatureState,
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

        let attemptLoadedState: StepQuizFeatureStepQuizStateAttemptLoaded? = {
            if let attemptLoadedState = state.stepQuizState as? StepQuizFeatureStepQuizStateAttemptLoaded {
                return attemptLoadedState
            } else if let attemptLoadingState = state.stepQuizState as? StepQuizFeatureStepQuizStateAttemptLoading {
                return attemptLoadingState.oldState
            }
            return nil
        }()

        if let attemptLoadedState {
            if case .unsupported = quizType {
                // it's rendered before step text
            } else {
                buildChildQuiz(quizType: quizType, step: step, attemptLoadedState: attemptLoadedState)
                if let formattedStats {
                    StepQuizStatsView(text: formattedStats)
                }

                StepQuizProblemsLimitView(
                    stateKs: viewModel.problemsLimitViewStateKs,
                    onReloadButtonTap: viewModel.doReloadProblemsLimit
                )

                buildQuizStatusView(state: state.stepQuizState, attemptLoadedState: attemptLoadedState)

                if let feedbackHintText {
                    StepQuizFeedbackView(text: feedbackHintText)
                }

                buildQuizActionButtons(quizType: quizType, state: state, attemptLoadedState: attemptLoadedState)
            }
        } else {
            StepQuizSkeletonViewFactory.makeSkeleton(for: quizType)
        }
    }

    @ViewBuilder
    private func buildChildQuiz(
        quizType: StepQuizChildQuizType,
        step: Step,
        attemptLoadedState: StepQuizFeatureStepQuizStateAttemptLoaded
    ) -> some View {
        if let dataset = attemptLoadedState.attempt.dataset {
            let submissionStateEmpty = attemptLoadedState.submissionState as? StepQuizFeatureSubmissionStateEmpty
            let submissionStateLoaded = attemptLoadedState.submissionState as? StepQuizFeatureSubmissionStateLoaded

            let reply = submissionStateLoaded?.submission.reply ?? submissionStateEmpty?.reply

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
        state: StepQuizFeatureState,
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

    private func handleViewAction(_ viewAction: StepQuizFeatureActionViewAction) {
        switch StepQuizFeatureActionViewActionKs(viewAction) {
        case .showNetworkError:
            ProgressHUD.showError(status: Strings.Common.connectionError)
        case .requestResetCode:
            presentResetCodePermissionAlert()
        case .showProblemsLimitReachedModal:
            presentProblemsLimitReachedModal()
        case .showParsonsProblemOnboardingModal:
            // TODO: ALTAPPS-952
            break
        case .problemsLimitViewAction:
            break
        case .navigateTo(let viewActionNavigateTo):
            switch StepQuizFeatureActionViewActionNavigateToKs(viewActionNavigateTo) {
            case .home:
                stackRouter.popViewController()
                TabBarRouter(tab: .home).route()
            case .stepScreen(let navigateToStepScreenViewAction):
                let assembly = StepAssembly(stepRoute: navigateToStepScreenViewAction.stepRoute)
                stackRouter.pushViewController(assembly.makeModule())
            }
        }
    }
}

// MARK: - StepQuizView (StepQuizUserPermissionRequest Alerts) -

extension StepQuizView {
    private func presentResetCodePermissionAlert() {
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
}

// MARK: - StepQuizView (Modals) -

extension StepQuizView {
    private func presentProblemsLimitReachedModal() {
        let panModal = ProblemsLimitReachedModalViewController(
            delegate: viewModel
        )

        panModalPresenter.presentPanModal(panModal)
    }
}
