import SwiftUI

struct StepQuizChoiceView: View {
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>

    @State var viewData: StepQuizChoiceViewData

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                StepQuizStatsView(text: viewData.formattedStats)

                StepQuizDescView(text: viewData.desc)

                StepQuizHintButton(
                    onClick: { print("onHintButtonClick") }
                )

                StepQuizChoicesView(
                    quizTitle: viewData.quizTitle,
                    choices: $viewData.choices,
                    isMultipleChoice: viewData.isMultipleChoice,
                    loading: true
                )

                if let quizStatus = viewData.quizStatus {
                    switch quizStatus {
                    case .evaluation:
                        StepQuizProgressView()
                    case .wrong:
                        StepQuizStatusView(state: .wrong)
                    case .correct:
                        StepQuizStatusView(state: .correct)
                    }

                    if let feedbackText = viewData.feedbackText {
                        StepQuizFeedbackView(text: feedbackText)
                    }
                }

                StepQuizActionButton(
                    state: .init(quizStatus: viewData.quizStatus),
                    onClick: { print("onQuizActionButtonClick") }
                )
            }
            .padding()

            StepQuizBottomControls(
                onShowDiscussionsClick: { print("onShowDiscussionsClick") }
            )
        }
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonTitleRemoved {
            self.presentationMode.wrappedValue.dismiss()
        }
        .navigationTitle(viewData.navigationTitle)
        .toolbar {
            NavigationToolbarInfoItem(
                onClick: { print("onToolbarInfoItemClick") }
            )
        }
    }
}

#if DEBUG
struct StepQuizChoiceView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            List {
                Section {
                    NavigationLink(
                        "Single Not solved",
                        destination: StepQuizChoiceView(
                            viewData: StepQuizChoiceViewData.makePlaceholder(isMultipleChoice: false)
                        )
                    )

                    NavigationLink(
                        "Single Correct",
                        destination: StepQuizChoiceView(
                            viewData: StepQuizChoiceViewData.makePlaceholder(
                                isMultipleChoice: false,
                                quizStatus: .correct
                            )
                        )
                    )

                    NavigationLink(
                        "Single Wrong",
                        destination: StepQuizChoiceView(
                            viewData: StepQuizChoiceViewData.makePlaceholder(
                                isMultipleChoice: false,
                                quizStatus: .wrong
                            )
                        )
                    )

                    NavigationLink(
                        "Single Evaluation",
                        destination: StepQuizChoiceView(
                            viewData: StepQuizChoiceViewData.makePlaceholder(
                                isMultipleChoice: false,
                                quizStatus: .evaluation
                            )
                        )
                    )
                }

                Section {
                    NavigationLink(
                        "Multiple Not solved",
                        destination: StepQuizChoiceView(
                            viewData: StepQuizChoiceViewData.makePlaceholder(isMultipleChoice: true)
                        )
                    )

                    NavigationLink(
                        "Multiple Correct",
                        destination: StepQuizChoiceView(
                            viewData: StepQuizChoiceViewData.makePlaceholder(
                                isMultipleChoice: true,
                                quizStatus: .correct
                            )
                        )
                    )

                    NavigationLink(
                        "Multiple Wrong",
                        destination: StepQuizChoiceView(
                            viewData: StepQuizChoiceViewData.makePlaceholder(isMultipleChoice: true, quizStatus: .wrong)
                        )
                    )

                    NavigationLink(
                        "Multiple Evaluation",
                        destination: StepQuizChoiceView(
                            viewData: StepQuizChoiceViewData.makePlaceholder(
                                isMultipleChoice: true,
                                quizStatus: .evaluation
                            )
                        )
                    )
                }
            }
            .navigationBarTitleDisplayMode(.inline)
        }
    }
}
#endif
