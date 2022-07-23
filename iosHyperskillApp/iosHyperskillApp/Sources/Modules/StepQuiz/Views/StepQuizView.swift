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
            .navigationBarBackButtonTitleRemoved { presentationMode.wrappedValue.dismiss() }
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

                    buildQuizContent(
                        state: viewModel.state,
                        step: viewModel.step,
                        quizName: viewData.quizName,
                        stepBlockName: viewData.stepBlockName
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
        quizName: String?,
        stepBlockName: String
    ) -> some View {
        if let quizName = quizName {
            StepQuizNameView(text: quizName)
        }

        let quizType = StepQuizChildQuizType(blockName: stepBlockName)

        if let attemptLoadedState = state as? StepQuizFeatureStateAttemptLoaded {
            if case .unsupported = quizType {
                StepQuizStatusView(state: .unsupportedQuiz)
            } else {
                buildChildQuiz(
                    for: quizType,
                    attemptLoadedState: attemptLoadedState,
                    step: step
                )
                buildQuizStatusView(attemptLoadedState: attemptLoadedState)
                buildQuizActionButton(attemptLoadedState: attemptLoadedState)
            }
        } else {
            StepQuizSkeletonViewFactory.makeSkeleton(for: quizType)
        }
    }

    @ViewBuilder
    private func buildChildQuiz(
        for quizType: StepQuizChildQuizType,
        attemptLoadedState: StepQuizFeatureStateAttemptLoaded,
        step: Step
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

            // TODO: Use here child quiz assembly instance when Swift 5.7 released
            Group {
                switch quizType {
                case .choice:
                    StepQuizChoiceAssembly(
                        step: step,
                        dataset: dataset,
                        reply: reply,
                        delegate: viewModel
                    )
                    .makeModule()
                case .code:
                    StepQuizCodeAssembly(
                        step: step,
                        dataset: dataset,
                        reply: reply,
                        delegate: viewModel
                    )
                    .makeModule()
                case .matching:
                    StepQuizMatchingAssembly(
                        step: step,
                        dataset: dataset,
                        reply: reply,
                        delegate: viewModel
                    )
                    .makeModule()
                case .sorting:
                    StepQuizSortingAssembly(
                        step: step,
                        dataset: dataset,
                        reply: reply,
                        delegate: viewModel
                    )
                    .makeModule()
                case .table:
                    StepQuizTableAssembly(
                        step: step,
                        dataset: dataset,
                        reply: reply,
                        delegate: viewModel
                    )
                    .makeModule()
                case .string, .number, .math:
                    StepQuizStringAssembly(
                        dataType: .init(quizType: quizType).require(),
                        step: step,
                        dataset: dataset,
                        reply: reply,
                        delegate: viewModel
                    )
                    .makeModule()
                case .unsupported(let blockName):
                    fatalError("Unsupported quiz = \(blockName)")
                }
            }
            .disabled(isDisabled)
        }
    }

    @ViewBuilder
    private func buildQuizStatusView(attemptLoadedState: StepQuizFeatureStateAttemptLoaded) -> some View {
        if let submissionStateLoaded = attemptLoadedState.submissionState as? StepQuizFeatureSubmissionStateLoaded,
           let submissionStatus = submissionStateLoaded.submission.status {
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
    private func buildQuizActionButton(attemptLoadedState: StepQuizFeatureStateAttemptLoaded) -> some View {
        let submissionStatus: SubmissionStatus? = {
            if let submissionStateLoaded = attemptLoadedState.submissionState as? StepQuizFeatureSubmissionStateLoaded {
                return submissionStateLoaded.submission.status
            }
            return SubmissionStatus.local
        }()

        StepQuizActionButton(
            state: .init(submissionStatus: submissionStatus),
            onTap: viewModel.doMainQuizAction
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
