import SwiftUI

struct QuizActionButton: View {
    var status: QuizStatus?
    var body: some View {
        if let status = status {
            Button(status.buttonText, action: {})
                .buttonStyle(RoundedRectangleButtonStyle(style: status.buttonStyle))
                .disabled(status == .evaluation)
        } else {
            Button(Strings.choiceQuizSendButtonText, action: {})
                .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
        }
    }
}

fileprivate extension QuizStatus {
    var buttonText: String {
        switch self {
        case .correct:
            return Strings.choiceQuizContinueButtonText
        case .wrong:
            return Strings.choiceQuizRetryButtonText
        case .evaluation:
            return Strings.choiceQuizCheckingButtonText
        }
    }

    var buttonStyle: RoundedRectangleButtonStyle.Style {
        switch self {
        case .correct:
            return .green
        case .wrong:
            return .violet
        case .evaluation:
            return .violet
        }
    }
}

struct QuizActionButton_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            QuizActionButton(status: nil)
            QuizActionButton(status: .correct)
            QuizActionButton(status: .wrong)
            QuizActionButton(status: .evaluation)
        }
    }
}
