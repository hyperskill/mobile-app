import SwiftUI

fileprivate extension QuizStatus {
    var image: String? {
        switch self {
        case .correct:
            return Images.StepQuiz.checkmark
        case .wrong:
            return Images.StepQuiz.info
        default:
            return nil
        }
    }

    var foregroundColor: Color? {
        switch self {
        case .correct:
            return Color(ColorPalette.secondary)
        case .wrong:
            return Color(ColorPalette.primary)
        default:
            return nil
        }
    }

    var backgroundColor: Color? {
        switch self {
        case .correct:
            return Color(ColorPalette.green200Alpha12)
        case .wrong:
            return Color(ColorPalette.onPrimary)
        default:
            return nil
        }
    }

    var paddingSet: Edge.Set? {
        switch self {
        case .correct:
            return .all
        case .wrong:
            return .vertical
        default:
            return nil
        }
    }
    var text: String? {
        switch self {
        case .correct:
            return Strings.choiceQuizCorrectStatusText
        case .wrong:
            return Strings.choiceQuizWrongStatusText
        default:
            return nil
        }
    }
}

struct QuizStatusView: View {
    var status: QuizStatus

    var body: some View {
        if
            let paddingSet = status.paddingSet,
            let image = status.image,
            let text = status.text,
            let backgroundColor = status.backgroundColor {
            HStack {
                Image(image)
                    .renderingMode(.template)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .foregroundColor(status.foregroundColor)
                    .frame(width: 20, height: 20)
                    .padding(.trailing, 16)
                Text(text)
                    .foregroundColor(status.foregroundColor)
                    .font(.body)
                Spacer()
            }
            .padding(paddingSet)
            .background(backgroundColor)
            .cornerRadius(8)
        }
    }
}

struct QuizStatusView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            QuizStatusView(status: .wrong)

            QuizStatusView(status: .correct)
        }
    }
}
