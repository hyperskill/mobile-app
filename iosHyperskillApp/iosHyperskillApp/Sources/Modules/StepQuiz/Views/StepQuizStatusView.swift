import shared
import SwiftUI

extension StepQuizStatusView {
    struct Appearance {
        let interItemSpacing = LayoutInsets.defaultInset

        let iconImageWidthHeight: CGFloat = 20

        let cornerRadius: CGFloat = 8
    }
}

struct StepQuizStatusView: View {
    private(set) var appearance = Appearance()

    let state: State

    var body: some View {
        HStack(spacing: appearance.interItemSpacing) {
            switch state {
            case .evaluation, .loading:
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle(tint: state.foregroundColor))
            default:
                Image(state.iconImageName)
                    .resizable()
                    .renderingMode(.template)
                    .aspectRatio(contentMode: .fit)
                    .foregroundColor(state.foregroundColor)
                    .frame(widthHeight: appearance.iconImageWidthHeight)
            }

            Text(state.title)
                .foregroundColor(state.foregroundColor)
                .font(.body)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(state.paddingEdgeSet)
        .background(state.backgroundColor)
        .cornerRadius(appearance.cornerRadius)
    }

    enum State: CaseIterable, Hashable {
        case correct
        case wrong
        case evaluation
        case loading
        case unsupportedQuiz
        case invalidReply(message: String)

        static var allCases: [StepQuizStatusView.State] {
            [
                .correct,
                .wrong,
                .evaluation,
                .loading,
                .unsupportedQuiz,
                .invalidReply(message: "Invalid reply")
            ]
        }

        fileprivate var iconImageName: String {
            switch self {
            case .correct:
                return Images.StepQuiz.checkmark
            case .wrong, .unsupportedQuiz, .invalidReply:
                return Images.StepQuiz.info
            case .evaluation, .loading:
                return ""
            }
        }

        fileprivate var title: String {
            switch self {
            case .correct:
                return Strings.StepQuiz.quizStatusCorrect
            case .wrong:
                return Strings.StepQuiz.quizStatusWrong
            case .evaluation:
                return Strings.StepQuiz.quizStatusEvaluation
            case .loading:
                return Strings.StepQuiz.quizStatusLoading
            case .unsupportedQuiz:
                return Strings.StepQuiz.unsupportedText
            case .invalidReply(let message):
                return message
            }
        }

        fileprivate var foregroundColor: Color {
            switch self {
            case .correct:
                return Color(ColorPalette.secondary)
            case .wrong, .evaluation, .loading, .unsupportedQuiz, .invalidReply:
                return Color(ColorPalette.primary)
            }
        }

        fileprivate var backgroundColor: Color {
            switch self {
            case .correct:
                return Color(ColorPalette.green200Alpha12)
            case .wrong:
                return .clear
            case .evaluation, .loading, .unsupportedQuiz, .invalidReply:
                return Color(ColorPalette.blue200Alpha12)
            }
        }

        fileprivate var paddingEdgeSet: Edge.Set {
            switch self {
            case .correct, .evaluation, .loading, .unsupportedQuiz, .invalidReply:
                return .all
            case .wrong:
                return .vertical
            }
        }
    }
}

// MARK: - StepQuizStatusView (SubmissionStatus) -

extension StepQuizStatusView {
    @ViewBuilder
    static func build(appearance: Appearance = Appearance(), submissionStatus: SubmissionStatus) -> some View {
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

// MARK: - StepQuizStatusView_Previews: PreviewProvider -

struct StepQuizStatusView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ForEach(StepQuizStatusView.State.allCases, id: \.self) { state in
                StepQuizStatusView(state: state)

                StepQuizStatusView(state: state)
                    .preferredColorScheme(.dark)
            }
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
