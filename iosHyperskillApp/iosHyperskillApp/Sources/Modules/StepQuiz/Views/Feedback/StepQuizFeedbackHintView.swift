import SwiftUI

struct StepQuizFeedbackHintView: View {
    let text: String

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
            Text(Strings.StepQuiz.feedbackTitle)
                .font(.caption)
                .foregroundColor(.tertiaryText)
                .frame(maxWidth: .infinity, alignment: .leading)

            LatexView(
                text: text,
                configuration: .quizContent(
                    textFont: .monospacedSystemFont(ofSize: 14, weight: .regular),
                    textColor: .primaryText,
                    backgroundColor: .clear
                )
            )
        }
        .padding()
        .background(Color.background)
        .addBorder()
    }
}

#if DEBUG
#Preview {
    ScrollView {
        StepQuizFeedbackHintView(
            text: """
That's right! Since any comparison results in a boolean value, there is no need to write everything twice.
"""
        )
    }
    .padding()
}
#endif
