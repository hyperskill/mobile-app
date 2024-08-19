import shared
import SwiftUI

struct StepQuizCodeBlanksSuggestionsView: View {
    let suggestions: [Suggestion]

    let isShineEffectActive: Bool

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
                            isActive: isShineEffectActive,
                            foregroundColor: Color(ColorPalette.primary)
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
        isShineEffectActive: true,
        onSuggestionTap: { _ in }
    )
}
#endif
