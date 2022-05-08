import SwiftUI

fileprivate extension QuizStatus {
    var buttonText: String {
        switch self {
        case .correct:
            return "Continue"
        case .wrong:
            return "Retry"
        }
    }

    var buttonStyle: RoundedRectangleButtonStyle.Style {
        switch self {
        case .correct:
            return .green
        case .wrong:
            return .violet
        }
    }
}

struct QuizActionButton: View {
    var status: QuizStatus?
    var body: some View {
        if let status = status {
            Button(status.buttonText, action: {})
                .buttonStyle(RoundedRectangleButtonStyle(style: status.buttonStyle))
        } else {
            Button("Send", action: {})
                .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
        }
        
    }
}

struct QuizActionButton_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            QuizActionButton(status: nil)
            QuizActionButton(status: .correct)
            QuizActionButton(status: .wrong)
        }
    }
}
