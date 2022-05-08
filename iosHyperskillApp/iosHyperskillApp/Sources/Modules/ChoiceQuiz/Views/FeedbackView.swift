import SwiftUI

struct FeedbackView: View {
    var text: String
    var body: some View {
        VStack(alignment: .leading) {
            Text("FEEDBACK:")
                    .font(.caption)
                    .foregroundColor(Color(ColorPalette.onSurfaceAlpha38))
            Text(text)
                .padding(.top, 8)
                .font(.system(size: 14, design: .monospaced))
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
