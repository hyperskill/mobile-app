import shared
import SwiftUI

extension StepQuizView {
    struct Appearance {
        let interItemSpacing: CGFloat = 20

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
            Text("Error")
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

                StepQuizHintButton(
                    onClick: { print("onHintButtonClick") }
                )

                if let quizName = viewData.quizName {
                    StepQuizNameView(text: quizName)
                }

//                if let quizStatus = viewData.quizStatus {
//                    switch quizStatus {
//                    case .evaluation:
//                        StepQuizProgressView()
//                    case .wrong:
//                        StepQuizStatusView(state: .wrong)
//                    case .correct:
//                        StepQuizStatusView(state: .correct)
//                    }
//
//                    if let feedbackText = viewData.feedbackText {
//                        StepQuizFeedbackView(text: feedbackText)
//                    }
//                }
//
//                StepQuizActionButton(
//                    state: .init(quizStatus: viewData.quizStatus),
//                    onClick: { print("onQuizActionButtonClick") }
//                )
            }
            .padding()

            StepQuizBottomControls(
                onShowDiscussionsClick: { print("onShowDiscussionsClick") }
            )
        }
    }

    private func handleViewAction(_ viewAction: StepQuizFeatureActionViewAction) {
        print("StepQuizView :: \(#function) viewAction = \(viewAction)")
    }
}

//struct StepQuizView_Previews: PreviewProvider {
//    static var previews: some View {
//        StepQuizAssembly().makeModule()
//    }
//}
