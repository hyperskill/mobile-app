import SwiftUI

struct FeedbackView: View {
    var text: String
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(Strings.choiceQuizFeedbackTitle)
                    .font(.caption)
                    .foregroundColor(Color(ColorPalette.onSurfaceAlpha38))
            Text(text)
                .font(.system(size: 14, design: .monospaced))
        }
        .padding()
        .background(Color(ColorPalette.background))
        .addBorder(color: Color(ColorPalette.onSurfaceAlpha12), width: 1, cornerRadius: 8)
    }
}

struct FeedbackView_Previews: PreviewProvider {
    static var previews: some View {
        FeedbackView(text: Strings.choiceQuizCorrectFeedbackText)
    }
}
