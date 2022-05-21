import SwiftUI

struct StepQuizActionButton: View {
    var state = State.default

    var onClick: (() -> Void)?

    var body: some View {
        Button(state.title, action: { onClick?() })
            .buttonStyle(RoundedRectangleButtonStyle(style: state.style))
            .disabled(state == .evaluation)
    }

    enum State: CaseIterable {
        case normal
        case correct
        case wrong
        case evaluation

        static let `default` = State.normal

        fileprivate var title: String {
            switch self {
            case .normal:
                return Strings.StepQuiz.sendButton
            case .correct:
                return Strings.StepQuiz.continueButton
            case .wrong:
                return Strings.StepQuiz.retryButton
            case .evaluation:
                return Strings.StepQuiz.checkingButton
            }
        }

        fileprivate var style: RoundedRectangleButtonStyle.Style {
            switch self {
            case .normal, .wrong, .evaluation:
                return .violet
            case .correct:
                return .green
            }
        }

        init(quizStatus: QuizStatus?) {
            switch quizStatus {
            case .none:
                self = .normal
            case .evaluation:
                self = .evaluation
            case .wrong:
                self = .wrong
            case .correct:
                self = .correct
            }
        }
    }
}

struct StepQuizActionButton_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ForEach(StepQuizActionButton.State.allCases, id: \.self) { state in
                StepQuizActionButton(state: state)
            }
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
