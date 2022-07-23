import SwiftUI

struct StepQuizActionButton: View {
    var state = State.default

    var titleForState: ((State) -> String?)?
    var systemImageNameForState: ((State) -> String)?

    var onTap: () -> Void

    private var overlayImage: RoundedRectangleButtonStyle.OverlayImage? {
        if let systemImageName = systemImageNameForState?(state) {
            return .init(imageSystemName: systemImageName)
        }
        return nil
    }

    var body: some View {
        Button(
            titleForState?(state) ?? state.title,
            action: onTap
        )
        .buttonStyle(RoundedRectangleButtonStyle(style: state.style, overlayImage: overlayImage))
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
    }
}

struct StepQuizActionButton_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ForEach(StepQuizActionButton.State.allCases, id: \.self) { state in
                StepQuizActionButton(state: state, onTap: {})
            }

            StepQuizActionButton(
                state: .normal,
                titleForState: { _ in "Run solution" },
                systemImageNameForState: { _ in "play" },
                onTap: {}
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
