import SwiftUI

fileprivate extension QuizStatus {

    var image: String {
        switch self {
        case .correct:
            return "check_icon"
        case .wrong:
            return "info_icon"
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
            return .white

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
}

struct QuizStatusView: View {
    var text: String
    var status: QuizStatus

    var body: some View {
        HStack {
            Image(status.image)
                .frame(width: 20, height: 20)
                .padding(.trailing, 16)
                .foregroundColor(.primary)
            Text(text)
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
            QuizStatusView(text: "Not correct, but keep on trying \nand never give up!", status: .wrong)

            QuizStatusView(text: "You’re absolutely correct!", status: .correct)
        }
    }
}
