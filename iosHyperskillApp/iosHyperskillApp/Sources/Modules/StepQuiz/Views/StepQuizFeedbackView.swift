import SwiftUI

struct StepQuizFeedbackView: View {
    var text: String

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
            Text(Strings.choiceQuizFeedbackTitle)
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
        StepQuizFeedbackView(text: Strings.choiceQuizCorrectFeedbackText)
            .previewLayout(.sizeThatFits)
            .padding()
    }
}
