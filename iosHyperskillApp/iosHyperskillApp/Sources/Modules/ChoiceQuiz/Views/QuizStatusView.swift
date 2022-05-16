import SwiftUI

fileprivate extension QuizStatus {
    var image: String {
        switch self {
        case .correct:
            return "choice-quiz-check-icon"
        case .wrong:
            return "choice-quiz-info-icon"
        }
    }

    var foregroundColor: Color {
        switch self {
        case .correct:
            return Color(ColorPalette.secondary)
        case .wrong:
            return Color(ColorPalette.primary)
        }
    }

    var backgroundColor: Color {
        switch self {
        case .correct:
            return Color(ColorPalette.green200Alpha12)
        case .wrong:
            return Color(ColorPalette.onPrimary)
        }
    }

    var paddingSet: Edge.Set {
        switch self {
        case .correct:
            return .all
        case .wrong:
            return .vertical
        }
    }
    var text: String {
        switch self {
        case .correct:
            return Strings.choiceQuizCorrectStatusText
        case .wrong:
            return Strings.choiceQuizWrongStatusText
        }
    }
}

struct QuizStatusView: View {
    var status: QuizStatus

    var body: some View {
        HStack {
            Image(status.image)
                .renderingMode(.template)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .foregroundColor(status.foregroundColor)
                .frame(width: 20, height: 20)
                .padding(.trailing, 16)
            Text(status.text)
                .foregroundColor(status.foregroundColor)
                .font(.body)
            Spacer()
        }
        .padding(status.paddingSet)
        .background(status.backgroundColor)
        .cornerRadius(8)
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
