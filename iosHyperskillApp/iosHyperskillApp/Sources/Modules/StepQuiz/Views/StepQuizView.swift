import shared
import SwiftUI

struct StepQuizView: View {
    @ObservedObject private var viewModel: StepQuizViewModel

    @Environment(\.presentationMode) private var presentationMode

    init(viewModel: StepQuizViewModel) {
        self.viewModel = viewModel
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        let viewData = viewModel.makeViewData()

        ScrollView {
            VStack(alignment: .leading, spacing: 20) {
                StepQuizStatsView(text: viewData.formattedStats)

                Text(viewData.text)
                    .font(.subheadline)
                    .foregroundColor(.primaryText)

                StepQuizHintButton(
                    onClick: { print("onHintButtonClick") }
                )

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
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonTitleRemoved {
            presentationMode.wrappedValue.dismiss()
        }
        .toolbar {
            NavigationToolbarInfoItem(
                onClick: { print("onToolbarInfoItemClick") }
            )
        }
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: StepQuizFeatureActionViewAction) {
        print("StepQuizView :: \(#function) viewAction = \(viewAction)")
    }
}

//struct StepQuizView_Previews: PreviewProvider {
//    static var previews: some View {
//        StepQuizAssembly().makeModule()
//    }
//}
