import SwiftUI

extension StepQuizFeedbackView {
    struct Appearance {
        let textFont = UIFont.monospacedSystemFont(ofSize: 14, weight: .regular)
        let textColor = UIColor.primaryText
    }
}

struct StepQuizFeedbackView: View {
    private(set) var appearance = Appearance()

    var text: String

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
            Text(Strings.StepQuiz.feedbackTitle)
                .font(.caption)
                .foregroundColor(.disabledText)

            LatexView(
                text: .constant(text),
                configuration: .init(
                    appearance: .init(labelFont: appearance.textFont, backgroundColor: .clear),
                    contentProcessor: ContentProcessor(
                        injections: ContentProcessor.defaultInjections + [
                            FontInjection(font: appearance.textFont),
                            TextColorInjection(dynamicColor: appearance.textColor)
                        ]
                    )
                )
            )
        }
        .frame(maxWidth: .infinity)
        .padding()
        .background(Color.background)
        .addBorder()
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
