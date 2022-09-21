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
            if state == .evaluation {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle(tint: state.foregroundColor))
            } else {
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

    enum State: CaseIterable, Equatable, Hashable {
        case correct
        case wrong(String)
        case evaluation
        case unsupportedQuiz

        fileprivate var iconImageName: String {
            switch self {
            case .correct:
                return Images.StepQuiz.checkmark
            case .wrong, .unsupportedQuiz:
                return Images.StepQuiz.info
            case .evaluation:
                return ""
            }
        }

        fileprivate var title: String {
            switch self {
            case .correct:
                return Strings.StepQuiz.quizStatusCorrect
            case .wrong(let message):
                return message
            case .evaluation:
                return Strings.StepQuiz.quizStatusEvaluation
            case .unsupportedQuiz:
                return Strings.StepQuiz.unsupportedText
            }
        }

        fileprivate var foregroundColor: Color {
            switch self {
            case .correct:
                return Color(ColorPalette.secondary)
            case .wrong, .evaluation, .unsupportedQuiz:
                return Color(ColorPalette.primary)
            }
        }

        fileprivate var backgroundColor: Color {
            switch self {
            case .correct:
                return Color(ColorPalette.green200Alpha12)
            case .wrong:
                return .clear
            case .evaluation, .unsupportedQuiz:
                return Color(ColorPalette.blue200Alpha12)
            }
        }

        fileprivate var paddingEdgeSet: Edge.Set {
            switch self {
            case .correct, .evaluation, .unsupportedQuiz:
                return .all
            case .wrong:
                return .vertical
            }
        }

        static var allCases: [StepQuizStatusView.State] {
            [
                .correct,
                .wrong(Strings.StepQuiz.quizStatusWrong),
                .unsupportedQuiz,
                .evaluation
            ]
        }
    }
}

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
