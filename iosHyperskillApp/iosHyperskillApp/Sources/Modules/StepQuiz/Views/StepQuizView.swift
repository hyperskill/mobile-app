import shared
import SwiftUI

extension StepQuizView {
    struct Appearance {
        let interItemSpacing = LayoutInsets.defaultInset

        let stepTextFont = UIFont.preferredFont(forTextStyle: .subheadline)
        let stepTextColor = UIColor.primaryText
    }
}

struct StepQuizView: View {
    private(set) var appearance = Appearance()

    @ObservedObject private var viewModel: StepQuizViewModel

    @Environment(\.presentationMode) private var presentationMode

    init(viewModel: StepQuizViewModel) {
        self.viewModel = viewModel
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        buildBody()
            .navigationBarTitleDisplayMode(.inline)
            .navigationBarBackButtonTitleRemoved {
                presentationMode.wrappedValue.dismiss()
            }
            .toolbar {
                NavigationToolbarInfoItem(
                    onClick: { print("onToolbarInfoItemClick") }
                )
            }
            .onAppear {
                viewModel.startListening()

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
                configuration: .networkError {
                    viewModel.loadAttempt(forceUpdate: true)
                }
            )
        } else {
            buildContent()
        }
    }

    @ViewBuilder
    private func buildContent() -> some View {
        let viewData = viewModel.makeViewData()

        ScrollView {
            LazyVStack(alignment: .leading, spacing: appearance.interItemSpacing) {
                StepQuizStatsView(text: viewData.formattedStats)

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

                StepQuizHintButton(onClick: { print("onHintButtonClick") })

                buildQuizContent(quizName: viewData.quizName, stepBlockName: viewData.stepBlockName)
            }
            .padding()

            StepQuizBottomControls(onShowDiscussionsClick: { print("onShowDiscussionsClick") })
        }
    }

    @ViewBuilder
    private func buildQuizContent(quizName: String?, stepBlockName: String) -> some View {
        if let quizName = quizName {
            StepQuizNameView(text: quizName)
        }

        if let attemptLoadedState = viewModel.state as? StepQuizFeatureStateAttemptLoaded {
            let quizType = StepQuizChildQuizType(blockName: stepBlockName)

            if case .unsupported = quizType {
                StepQuizStatusView(state: .unsupportedQuiz)
            } else {
                buildChildQuiz(for: quizType, attemptLoadedState: attemptLoadedState)

                if let loadedSubmission = attemptLoadedState.submissionState as? StepQuizFeatureSubmissionStateLoaded {
                    buildQuizStatusView(submissionLoadedState: loadedSubmission)

                    buildQuizActionButton(
                        attemptLoadedState: attemptLoadedState,
                        submissionLoadedState: loadedSubmission
                    )
                }
            }
        } else {
            ProgressView()
                .frame(maxWidth: .infinity, alignment: .center)
        }
    }

    @ViewBuilder
    private func buildChildQuiz(
        for quizType: StepQuizChildQuizType,
        attemptLoadedState: StepQuizFeatureStateAttemptLoaded
    ) -> some View {
        if let dataset = attemptLoadedState.attempt.dataset {
            let submissionStateEmpty = attemptLoadedState.submissionState as? StepQuizFeatureSubmissionStateEmpty
            let submissionStateLoaded = attemptLoadedState.submissionState as? StepQuizFeatureSubmissionStateLoaded

            let reply = submissionStateLoaded?.submission.reply ?? submissionStateEmpty?.reply
            let isDisabled: Bool = {
                if let submissionStateLoaded = submissionStateLoaded {
                    return !submissionStateLoaded.submission.isSubmissionEditable
                }
                return false
            }()

            Group {
                switch quizType {
                case .choice:
                    StepQuizChoiceAssembly(dataset: dataset, reply: reply, delegate: viewModel).makeModule()
                case .unsupported(let blockName):
                    fatalError("Unsupported quiz = \(blockName)")
                }
            }
            .disabled(isDisabled)
        }
    }

    @ViewBuilder
    private func buildQuizStatusView(submissionLoadedState: StepQuizFeatureSubmissionStateLoaded) -> some View {
        if let submissionStatus = submissionLoadedState.submission.status {
            switch submissionStatus {
            case SubmissionStatus.evaluation:
                StepQuizStatusView(state: .evaluation)
            case SubmissionStatus.wrong:
                StepQuizStatusView(state: .wrong)
            case SubmissionStatus.correct:
                StepQuizStatusView(state: .correct)
            default:
                EmptyView()
            }
        }
    }

    @ViewBuilder
    private func buildQuizActionButton(
        attemptLoadedState: StepQuizFeatureStateAttemptLoaded,
        submissionLoadedState: StepQuizFeatureSubmissionStateLoaded
    ) -> some View {
        StepQuizActionButton(
            state: .init(submissionStatus: submissionLoadedState.submission.status),
            onClick: viewModel.doMainQuizAction
        )
        .disabled(!StepQuizResolver.shared.isQuizEnabled(state: attemptLoadedState))
    }

    private func handleViewAction(_ viewAction: StepQuizFeatureActionViewAction) {
        switch viewAction {
        case is StepQuizFeatureActionViewActionShowNetworkError:
            ProgressHUD.showError(status: Strings.General.connectionError)
        default:
            print("StepQuizView :: unhandled viewAction = \(viewAction)")
        }
    }
}
