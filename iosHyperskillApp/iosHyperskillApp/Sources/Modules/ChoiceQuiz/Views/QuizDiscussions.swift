import SwiftUI

struct QuizDiscussions: View {
    var body: some View {
        VStack {
            Divider()

            Button(action: {}, label: {
                HStack {
                    Text("Show discussions")
                }
                .padding(.vertical, 0)
            })
            .buttonStyle(OutlineButtonStyle(font: .subheadline))
            .overlay(
                Image("chat_icon").padding(.leading),
                alignment: .leading
            )
            .padding()
        }
        .frame(height: .infinity)
        .background(Color(ColorPalette.background).ignoresSafeArea())
    }
}

struct QuizDiscussions_Previews: PreviewProvider {
    static var previews: some View {
        QuizDiscussions()
    }
}
