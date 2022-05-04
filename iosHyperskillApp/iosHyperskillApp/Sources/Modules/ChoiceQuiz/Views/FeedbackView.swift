import SwiftUI

struct FeedbackView: View {
    var text: String
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                Text("FEEDBACK:")
                    .font(.caption)
                    .foregroundColor(Color(ColorPalette.onSurfaceAlpha38))
                Spacer()
            }
            //todo здесь шрифт вроде должен быть другой, но не нашел его
            Text(text)
                .padding(.top, 8)
        }
        .padding()
        .background(Color(ColorPalette.background))
        .addBorder(color: Color(ColorPalette.onSurfaceAlpha12), width: 1, cornerRadius: 8)
    }
}

struct FeedbackView_Previews: PreviewProvider {
    static var previews: some View {
        FeedbackView(text: "That's right! Since any comparison results in a boolean value, there is no need to write everything twice.")
    }
}
