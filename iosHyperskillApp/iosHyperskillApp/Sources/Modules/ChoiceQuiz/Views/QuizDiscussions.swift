import SwiftUI

struct QuizDiscussions: View {
    var body: some View {
        VStack {
            Divider()

            Button(action: {}, label: {
                Text(Strings.choiceQuizDiscussionsButtonText)
                    .font(.body)
                    .frame(maxWidth: .infinity, minHeight: 44, alignment: .center)
                    .overlay(
                        Image("choice-quiz-chat-icon")
                            .renderingMode(.template)
                            .frame(width: 20, height: 20)
                            .padding(.leading, 18)
                            .foregroundColor(Color(ColorPalette.primary)),
                        alignment: .leading
                    )
            })
            .padding()
            .buttonStyle(OutlineButtonStyle())
        }
        .background(Color(ColorPalette.background).ignoresSafeArea())
    }
}

struct QuizDiscussions_Previews: PreviewProvider {
    static var previews: some View {
        QuizDiscussions()
    }
}
