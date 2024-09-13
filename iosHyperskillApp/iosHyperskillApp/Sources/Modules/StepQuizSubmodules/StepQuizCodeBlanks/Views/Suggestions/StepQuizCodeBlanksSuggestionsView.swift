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
                        StepQuizCodeBlanksCodeBlockChildTextView(
                            text: suggestion.text,
                            isActive: true
                        )
                        .shineEffect(
                            isActive: isAnimationEffectActive,
                            foregroundColor: Color(ColorPalette.primary)
                        )
                        .pulseEffect(
                            shape: RoundedRectangle(
                                cornerRadius: StepQuizCodeBlanksCodeBlockChildTextView.Appearance.cornerRadius
                            ),
                            isActive: isAnimationEffectActive
                        )
                    }
                )
                .buttonStyle(BounceButtonStyle())
            }

            // Preserve height to avoid layout jumps
            if suggestions.isEmpty {
                StepQuizCodeBlanksCodeBlockChildTextView(text: "", isActive: false)
                    .hidden()
            }
        }
        .padding(LayoutInsets.defaultInset)
    }
}

extension StepQuizCodeBlanksSuggestionsView: Equatable {
    static func == (lhs: StepQuizCodeBlanksSuggestionsView, rhs: StepQuizCodeBlanksSuggestionsView) -> Bool {
        lhs.isAnimationEffectActive == rhs.isAnimationEffectActive &&
        lhs.suggestions.map(\.text) == rhs.suggestions.map(\.text)
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
