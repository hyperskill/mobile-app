import shared
import SwiftUI

struct StepQuizCodeBlanksSuggestionsView: View {
    let suggestions: [Suggestion]

    let isAnimationEffectActive: Bool

    let onSuggestionTap: (Suggestion) -> Void

    var body: some View {
        FlowLayoutCompatibility(
            configuration: .init(
                spacing: LayoutInsets.defaultInset,
                fallbackLayout: .vertical()
            )
        ) {
            ForEach(suggestions, id: \.self) { suggestion in
                Button(
                    action: {
                        onSuggestionTap(suggestion)
                    },
                    label: {
                        StepQuizCodeBlanksOptionView(
                            text: suggestion.text,
                            isActive: true
                        )
                        .shineEffect(
                            isActive: isAnimationEffectActive,
                            foregroundColor: Color(ColorPalette.primary)
                        )
                        .pulseEffect(
                            shape: RoundedRectangle(
                                cornerRadius: StepQuizCodeBlanksOptionView.Appearance.cornerRadius
                            ),
                            isActive: isAnimationEffectActive
                        )
                    }
                )
                .buttonStyle(BounceButtonStyle())
            }
        }
        .padding(LayoutInsets.defaultInset)
        .frame(minHeight: 72)
    }
}

#if DEBUG
#Preview {
    StepQuizCodeBlanksSuggestionsView(
        suggestions: [
            Suggestion.ConstantString(text: "Hello world")
        ],
        isAnimationEffectActive: true,
        onSuggestionTap: { _ in }
    )
}
#endif
