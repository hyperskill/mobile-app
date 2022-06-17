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
            buildChildQuiz(stepBlockName: stepBlockName, attemptLoadedState: attemptLoadedState)

            if let submissionLoadedState = attemptLoadedState.submissionState as? StepQuizFeatureSubmissionStateLoaded {
                buildQuizStatusView(submissionLoadedState: submissionLoadedState)

                buildQuizActionButton(submissionLoadedState: submissionLoadedState)
            }
        } else {
            ProgressView()
                .frame(maxWidth: .infinity, alignment: .center)
        }
    }

    @ViewBuilder
    private func buildChildQuiz(
        stepBlockName: String,
        attemptLoadedState: StepQuizFeatureStateAttemptLoaded
    ) -> some View {
        let quizType = StepQuizChildQuizType(blockName: stepBlockName)

        if case let .unsupported(blockName) = quizType {
            Text("Unsupported quiz = \(blockName)")
        } else if let dataset = attemptLoadedState.attempt.dataset {
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
    private func buildQuizActionButton(submissionLoadedState: StepQuizFeatureSubmissionStateLoaded) -> some View {
        StepQuizActionButton(
            state: { () -> StepQuizActionButton.State in
                guard let submissionStatus = submissionLoadedState.submission.status else {
                    return .normal
                }

                switch submissionStatus {
                case SubmissionStatus.evaluation:
                    return .evaluation
                case SubmissionStatus.wrong:
                    return .wrong
                case SubmissionStatus.correct:
                    return .correct
                case SubmissionStatus.outdated:
                    return .wrong
                case SubmissionStatus.local:
                    return .normal
                default:
                    return .normal
                }
            }(),
            onClick: viewModel.doMainQuizAction
        )
    }

    private func handleViewAction(_ viewAction: StepQuizFeatureActionViewAction) {
        print("StepQuizView :: \(#function) viewAction = \(viewAction)")
    }
}
