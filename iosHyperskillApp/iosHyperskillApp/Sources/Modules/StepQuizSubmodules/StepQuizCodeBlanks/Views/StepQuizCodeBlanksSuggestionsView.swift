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

            // Preserve height to avoid layout jumps
            if suggestions.isEmpty {
                StepQuizCodeBlanksOptionView(text: "", isActive: false)
                    .hidden()
            }
        }
        .padding(LayoutInsets.defaultInset)
    }
}

#if DEBUG
#Preview {
    VStack {
        StepQuizCodeBlanksSuggestionsView(
            suggestions: [
                Suggestion.ConstantString(text: "128"),
                Suggestion.ConstantString(text: "5")
            ],
            isAnimationEffectActive: true,
            onSuggestionTap: { _ in }
        )

        StepQuizCodeBlanksSuggestionsView(
            suggestions: [],
            isAnimationEffectActive: true,
            onSuggestionTap: { _ in }
        )
    }
}
#endif
