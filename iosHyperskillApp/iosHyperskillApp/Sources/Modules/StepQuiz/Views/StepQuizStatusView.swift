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

    var state: State

    var body: some View {
        HStack(spacing: appearance.interItemSpacing) {
            Image(state.iconImageName)
                .resizable()
                .renderingMode(.template)
                .aspectRatio(contentMode: .fit)
                .foregroundColor(state.foregroundColor)
                .frame(widthHeight: appearance.iconImageWidthHeight)

            Text(state.title)
                .foregroundColor(state.foregroundColor)
                .font(.body)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(state.paddingEdgeSet)
        .background(state.backgroundColor)
        .cornerRadius(appearance.cornerRadius)
    }

    enum State: CaseIterable {
        case correct
        case wrong

        fileprivate var iconImageName: String {
            switch self {
            case .correct:
                return Images.StepQuiz.checkmark
            case .wrong:
                return Images.StepQuiz.info
            }
        }

        fileprivate var title: String {
            switch self {
            case .correct:
                return Strings.StepQuiz.quizStatusCorrect
            case .wrong:
                return Strings.StepQuiz.quizStatusWrong
            }
        }

        fileprivate var foregroundColor: Color {
            switch self {
            case .correct:
                return Color(ColorPalette.secondary)
            case .wrong:
                return Color(ColorPalette.primary)
            }
        }

        fileprivate var backgroundColor: Color {
            switch self {
            case .correct:
                return Color(ColorPalette.green200Alpha12)
            case .wrong:
                return .clear
            }
        }

        fileprivate var paddingEdgeSet: Edge.Set {
            switch self {
            case .correct:
                return .all
            case .wrong:
                return .vertical
            }
        }
    }
}

struct StepQuizStatusView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ForEach(StepQuizStatusView.State.allCases, id: \.self) { state in
                StepQuizStatusView(state: state)
            }
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
