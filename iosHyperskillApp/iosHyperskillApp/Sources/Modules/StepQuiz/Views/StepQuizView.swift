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
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    if viewModel.stepRoute is StepRouteRepeat,
                       let theoryID = viewModel.step.topicTheory {
                        Button(Strings.Step.theory) {
                            let assembly = StepAssembly(stepRoute: StepRouteRepeat(stepId: theoryID.int64Value))
                            stackRouter.pushViewController(assembly.makeModule())
                        }
                    }
                }
            }
            .onAppear {
                viewModel.startListening()
                viewModel.onViewAction = handleViewAction(_:)

                if viewModel.state is StepQuizFeatureStateIdle {
                    viewModel.loadAttempt()
                }

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
        if viewModel.state is StepQuizFeatureStateNetworkError {
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: .clear,
                    action: {
                        viewModel.loadAttempt(forceUpdate: true)
                    }
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
                            StepQuizHintsAssembly(stepID: viewModel.step.id).makeModule()
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

        let attemptLoadedState: StepQuizFeatureStateAttemptLoaded? = {
            if let attemptLoadedState = state as? StepQuizFeatureStateAttemptLoaded {
                return attemptLoadedState
            } else if let attemptLoadingState = state as? StepQuizFeatureStateAttemptLoading {
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
                buildQuizStatusView(state: state, attemptLoadedState: attemptLoadedState)

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
        attemptLoadedState: StepQuizFeatureStateAttemptLoaded
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
        state: StepQuizFeatureState,
        attemptLoadedState: StepQuizFeatureStateAttemptLoaded
    ) -> some View {
        if state is StepQuizFeatureStateAttemptLoading {
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
        attemptLoadedState: StepQuizFeatureStateAttemptLoaded
    ) -> some View {
        let submissionStatus: SubmissionStatus? = {
            if let submissionStateLoaded = attemptLoadedState.submissionState as? StepQuizFeatureSubmissionStateLoaded {
                return submissionStateLoaded.submission.status
            }
            return SubmissionStatus.local
        }()

        if submissionStatus == SubmissionStatus.wrong {
            if quizType.isCodeRelated {
                StepQuizActionButtons.retryLogoAndRunSolution(
                    retryButtonAction: viewModel.doQuizRetryAction,
                    runSolutionButtonState: .init(submissionStatus: submissionStatus),
                    runSolutionButtonAction: viewModel.doMainQuizAction
                )
                .disabled(StepQuizResolver.shared.isQuizLoading(state: state))
            } else if StepQuizResolver.shared.isNeedRecreateAttemptForNewSubmission(step: viewModel.step) {
                StepQuizActionButtons.retry(action: viewModel.doQuizRetryAction)
                    .disabled(StepQuizResolver.shared.isQuizLoading(state: state))
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
                .disabled(StepQuizResolver.shared.isQuizLoading(state: state) || viewModel.isPracticingLoading)
            } else {
                StepQuizActionButtons.continue(
                    isLoading: viewModel.isPracticingLoading,
                    action: viewModel.doQuizContinueAction
                )
                .disabled(StepQuizResolver.shared.isQuizLoading(state: state))
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
            ProgressHUD.showError(status: Strings.General.connectionError)
        case .requestUserPermission(let requestUserPermissionViewAction):
            switch requestUserPermissionViewAction.userPermissionRequest {
            case StepQuizUserPermissionRequest.resetCode:
                presentResetCodePermissionAlert()
            case StepQuizUserPermissionRequest.sendDailyStudyReminders:
                presentSendDailyStudyRemindersPermissionAlert()
            default:
                break
            }
        case .showProblemOfDaySolvedModal(let showProblemOfDaySolvedModalViewAction):
            presentDailyStepCompletedModal(earnedGemsText: showProblemOfDaySolvedModalViewAction.earnedGemsText)
        case .navigateTo(let viewActionNavigateTo):
            switch StepQuizFeatureActionViewActionNavigateToKs(viewActionNavigateTo) {
            case .back:
                stackRouter.popViewController()
            }
        }
    }
}

// MARK: - StepQuizView (StepQuizUserPermissionRequest Alerts) -

extension StepQuizView {
    private func presentResetCodePermissionAlert() {
        let alert = UIAlertController(
            title: viewModel.makeUserPermissionRequestTitle(StepQuizUserPermissionRequest.resetCode),
            message: viewModel.makeUserPermissionRequestMessage(StepQuizUserPermissionRequest.resetCode),
            preferredStyle: .alert
        )
        alert.addAction(
            UIAlertAction(
                title: Strings.General.cancel,
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

    private func presentSendDailyStudyRemindersPermissionAlert() {
        let alert = UIAlertController(
            title: viewModel.makeUserPermissionRequestTitle(StepQuizUserPermissionRequest.sendDailyStudyReminders),
            message: viewModel.makeUserPermissionRequestMessage(StepQuizUserPermissionRequest.sendDailyStudyReminders),
            preferredStyle: .alert
        )
        alert.addAction(
            UIAlertAction(
                title: Strings.General.ok,
                style: .default,
                handler: { [weak viewModel] _ in
                    viewModel?.handleSendDailyStudyRemindersPermissionRequestResult(isGranted: true)
                }
            )
        )
        alert.addAction(
            UIAlertAction(
                title: Strings.General.later,
                style: .cancel,
                handler: { [weak viewModel] _ in
                    viewModel?.handleSendDailyStudyRemindersPermissionRequestResult(isGranted: false)
                }
            )
        )

        modalRouter.presentAlert(alert)
    }
}

// MARK: - StepQuizView (Modals) -

extension StepQuizView {
    private func presentDailyStepCompletedModal(earnedGemsText: String) {
        viewModel.logDailyStepCompletedModalShownEvent()

        let panModal = ProblemOfDaySolvedModalViewController(
            earnedGemsText: earnedGemsText,
            onGoBackButtonTap: { [weak viewModel] in
                viewModel?.doGoBackAction()
            }
        )
        panModal.onDisappear = { [weak viewModel] in
            viewModel?.logDailyStepCompletedModalHiddenEvent()
        }

        panModalPresenter.presentPanModal(panModal)
    }
}
