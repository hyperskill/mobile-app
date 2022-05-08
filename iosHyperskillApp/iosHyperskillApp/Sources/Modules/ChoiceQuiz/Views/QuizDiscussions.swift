import SwiftUI

struct QuizDiscussions: View {
    var body: some View {
        VStack {
            Divider()

            Button(action: {}, label: {
                Text("Show discussions")
                    .font(.body)
                    .frame(maxWidth: .infinity, minHeight: 44, alignment: .center)
                    .overlay(
                        Image("chat_icon")
                            .frame(width: 20, height: 20)
                            .padding(.leading, 18)
                            .foregroundColor(.primary),
                        alignment: .leading
                    )
            })
            .padding()
            .buttonStyle(OutlineButtonStyle())
            .buttonStyle(BounceButtonStyle())
        }
        .background(Color(ColorPalette.background).ignoresSafeArea())
    }
}

struct QuizDiscussions_Previews: PreviewProvider {
    static var previews: some View {
        QuizDiscussions()
    }
}
