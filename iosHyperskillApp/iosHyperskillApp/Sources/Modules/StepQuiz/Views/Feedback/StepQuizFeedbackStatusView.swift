import shared
import SwiftUI

extension StepQuizFeedbackStatusView {
    struct Appearance {
        let interItemSpacing = LayoutInsets.defaultInset

        let iconImageWidthHeight: CGFloat = 20

        let cornerRadius: CGFloat = 8
    }
}

struct StepQuizFeedbackStatusView: View {
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
        .padding()
        .background(state.backgroundColor)
        .cornerRadius(appearance.cornerRadius)
    }

    enum State: CaseIterable, Hashable {
        case correct
        case wrong
        case evaluation
        case loading
        case invalidReply(message: String)

        static var allCases: [StepQuizFeedbackStatusView.State] {
            [
                .correct,
                .wrong,
                .evaluation,
                .loading,
                .invalidReply(message: "Invalid reply")
            ]
        }

        fileprivate var iconImageName: String {
            switch self {
            case .correct:
                Images.StepQuiz.checkmark
            case .wrong, .invalidReply:
                Images.StepQuiz.info
            case .evaluation, .loading:
                ""
            }
        }

        fileprivate var title: String {
            switch self {
            case .correct:
                Strings.StepQuiz.quizStatusCorrect
            case .wrong:
                Strings.StepQuiz.quizStatusWrong
            case .evaluation:
                Strings.StepQuiz.quizStatusEvaluation
            case .loading:
                Strings.StepQuiz.quizStatusLoading
            case .invalidReply(let message):
                message
            }
        }

        fileprivate var foregroundColor: Color {
            switch self {
            case .correct:
                Color(ColorPalette.secondary)
            case .wrong, .evaluation, .loading, .invalidReply:
                Color(ColorPalette.primary)
            }
        }

        fileprivate var backgroundColor: Color {
            switch self {
            case .correct:
                Color(ColorPalette.green200Alpha12)
            case .evaluation, .loading, .invalidReply, .wrong:
                Color(ColorPalette.blue200Alpha12)
            }
        }
    }
}

// MARK: - Preview -

#if DEBUG
#Preview {
    Group {
        ForEach(StepQuizFeedbackStatusView.State.allCases, id: \.self) { state in
            StepQuizFeedbackStatusView(state: state)
        }
    }
    .padding()
}

#Preview {
    Group {
        ForEach(StepQuizFeedbackStatusView.State.allCases, id: \.self) { state in
            StepQuizFeedbackStatusView(state: state)
                .preferredColorScheme(.dark)
        }
    }
    .padding()
}
#endif
