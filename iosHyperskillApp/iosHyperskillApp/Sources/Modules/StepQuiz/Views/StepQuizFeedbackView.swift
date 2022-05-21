import SwiftUI

struct StepQuizFeedbackView: View {
    var text: String

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
            Text(Strings.StepQuiz.feedbackTitle)
                .font(.caption)
                .foregroundColor(.disabledText)

            Text(text)
                .font(.system(size: 14, design: .monospaced))
                .foregroundColor(.primaryText)
        }
        .frame(maxWidth: .infinity)
        .padding()
        .background(Color.background)
        .addBorder(color: Color(ColorPalette.onSurfaceAlpha12))
    }
}

struct StepQuizFeedbackView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizFeedbackView(
            text: """
That's right! Since any comparison results in a boolean value, there is no need to write everything twice.
"""
        )
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
