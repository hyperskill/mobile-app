import SwiftUI

struct StepQuizActionButton: View {
    var state = State.default

    var titleForState: ((State) -> String?)?
    var systemImageNameForState: ((State) -> String?)?

    var onTap: () -> Void

    private var overlayImage: RoundedRectangleButtonStyle.OverlayImage? {
        if let systemImageName = systemImageNameForState?(state) {
            return .init(imageSystemName: systemImageName)
        }
        return nil
    }

    var body: some View {
        Button(
            action: onTap,
            label: {
                HStack(spacing: LayoutInsets.smallInset) {
                    if state == .correctLoading {
                        ProgressView()
                    }

                    Text(titleForState?(state) ?? state.title)
                }
            }
        )
        .buttonStyle(RoundedRectangleButtonStyle(style: state.style, overlayImage: overlayImage))
        .disabled(state == .evaluation || state == .correctLoading)
    }

    enum State: CaseIterable {
        case normal
        case correct
        case correctLoading
        case wrong
        case evaluation

        static let `default` = State.normal

        fileprivate var title: String {
            switch self {
            case .normal:
                return Strings.StepQuiz.sendButton
            case .correct, .correctLoading:
                return Strings.StepQuiz.continueButton
            case .wrong:
                return Strings.StepQuiz.sendButton // .retryButton
            case .evaluation:
                return Strings.StepQuiz.checkingButton
            }
        }

        fileprivate var style: RoundedRectangleButtonStyle.Style {
            switch self {
            case .normal, .wrong, .evaluation:
                return .violet
            case .correct, .correctLoading:
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
