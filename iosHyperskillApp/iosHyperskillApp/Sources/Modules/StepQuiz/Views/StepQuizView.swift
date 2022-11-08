import shared
import SwiftUI
import UIKit

extension StepQuizView {
    struct Appearance {
        let interItemSpacing = LayoutInsets.defaultInset

        let stepTextFont = UIFont.preferredFont(forTextStyle: .subheadline)
        let stepTextColor = UIColor.primaryText
    }
}

struct StepQuizView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: StepQuizViewModel

    @EnvironmentObject private var modalRouter: SwiftUIModalRouter

    @Environment(\.presentationMode) private var presentationMode

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            buildBody()
        }
        .navigationBarTitleDisplayMode(.inline)
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)

            if viewModel.state is StepQuizFeatureStateIdle {
                viewModel.loadAttempt()
            }
        }
        .onDisappear(perform: viewModel.stopListening)
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

            let quizType = StepQuizChildQuizType(blockName: viewData.stepBlockName)

            ScrollView {
                LazyVStack(alignment: .leading, spacing: appearance.interItemSpacing) {
                    StepQuizStatsView(text: viewData.formattedStats)

                    if case .unsupported = quizType {
                        StepQuizStatusView(state: .unsupportedQuiz)
                    }

                    LatexView(
                        text: .constant(viewData.stepText),
                        configuration: .init(
                            appearance: .init(labelFont: appearance.stepTextFont),
                            contentProcessor: ContentProcessor(
                                injections: ContentProcessor.defaultInjections + [
                                    StepStylesInjection(),
                                    FontInjection(font: appearance.stepTextFont),
                                    TextColorInjection(dynamicColor: appearance.stepTextColor)
                                ]
                            )
                        )
                    )

                    if viewData.stepHasHints {
                        StepQuizHintsAssembly(stepID: viewModel.step.id).makeModule()
                    }

                    buildQuizContent(
                        state: viewModel.state,
                        step: viewModel.step,
                        stepQuizName: viewData.quizName,
                        quizType: quizType,
                        feedbackHintText: viewData.feedbackHintText
                    )
                }
                .padding()
            }
        }
    }

    @ViewBuilder
    private func buildQuizContent(
        state: StepQuizFeatureState,
        step: Step,
        stepQuizName: String?,
        quizType: StepQuizChildQuizType,
        feedbackHintText: String?
    ) -> some View {
        if let stepQuizName = stepQuizName {
            StepQuizNameView(text: stepQuizName)
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
                buildQuizStatusView(state: state, attemptLoadedState: attemptLoadedState)

                if let feedbackHintText = feedbackHintText {
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
            let isDisabled: Bool = {
                guard let submissionStatus = submissionStateLoaded?.submission.status else {
                    return false
                }

                switch submissionStatus {
                case SubmissionStatus.evaluation, SubmissionStatus.correct, SubmissionStatus.outdated:
                    return true
                case SubmissionStatus.wrong:
                    return StepQuizResolver.shared.isNeedRecreateAttemptForNewSubmission(step: viewModel.step)
                case SubmissionStatus.local:
                    return false
                default:
                    return false
                }
            }()

            StepQuizChildQuizViewFactory.make(
                quizType: quizType,
                step: step,
                dataset: dataset,
                reply: reply,
                provideModuleInputCallback: { viewModel.childQuizModuleInput = $0 },
                moduleOutput: viewModel
            )
            .disabled(isDisabled)
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
            if case .code = quizType {
                StepQuizActionButtons.retryLogoAndRunSolution(
                    retryButtonAction: viewModel.doQuizRetryAction,
                    runSolutionButtonState: .init(submissionStatus: submissionStatus),
                    runSolutionButtonAction: viewModel.doMainQuizAction
                )
                .disabled(StepQuizResolver.shared.isQuizLoading(state: state))
            } else if StepQuizResolver.shared.isNeedRecreateAttemptForNewSubmission(step: viewModel.step) {
                StepQuizActionButtons
                    .retry(action: viewModel.doQuizRetryAction)
                    .disabled(StepQuizResolver.shared.isQuizLoading(state: state))
            } else {
                StepQuizActionButtons
                    .submit(state: .init(submissionStatus: submissionStatus), action: viewModel.doMainQuizAction)
                    .disabled(!StepQuizResolver.shared.isQuizEnabled(state: attemptLoadedState))
            }
        } else if submissionStatus == SubmissionStatus.correct {
            if StepQuizResolver.shared.isQuizRetriable(step: viewModel.step) {
                StepQuizActionButtons.retryLogoAndContinue(
                    retryButtonAction: viewModel.doQuizRetryAction,
                    continueButtonAction: viewModel.doQuizContinueAction
                )
                .disabled(StepQuizResolver.shared.isQuizLoading(state: state))
            } else {
                StepQuizActionButtons
                    .continue(action: viewModel.doQuizContinueAction)
                    .disabled(StepQuizResolver.shared.isQuizLoading(state: state))
            }
        } else {
            StepQuizActionButtons.submit(
                state: .init(submissionStatus: submissionStatus),
                action: viewModel.doMainQuizAction
            )
            .disabled(!StepQuizResolver.shared.isQuizEnabled(state: attemptLoadedState))
        }
    }

    private func handleViewAction(_ viewAction: StepQuizFeatureActionViewAction) {
        switch viewAction {
        case is StepQuizFeatureActionViewActionShowNetworkError:
            ProgressHUD.showError(status: Strings.General.connectionError)
        case let requestUserPermissionViewAction as StepQuizFeatureActionViewActionRequestUserPermission:
            switch requestUserPermissionViewAction.userPermissionRequest {
            case StepQuizUserPermissionRequest.resetCode:
                presentResetCodePermissionAlert()
            case StepQuizUserPermissionRequest.sendDailyStudyReminders:
                presentSendDailyStudyRemindersPermissionAlert()
            default:
                break
            }
        case is StepQuizFeatureActionViewActionNavigateToHomeScreen:
            presentationMode.wrappedValue.dismiss()
        default:
            print("StepQuizView :: unhandled viewAction = \(viewAction)")
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
