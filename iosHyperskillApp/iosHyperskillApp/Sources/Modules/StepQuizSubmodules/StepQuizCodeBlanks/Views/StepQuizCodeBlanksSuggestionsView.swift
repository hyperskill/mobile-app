import shared
import SwiftUI

struct StepQuizCodeBlanksSuggestionsView: View {
    let suggestions: [Suggestion]

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
                        StepQuizCodeBlanksOptionView(text: suggestion.text, isActive: true)
                    }
                )
                .buttonStyle(BounceButtonStyle())
            }
        }
        .padding(LayoutInsets.defaultInset)
        .frame(minHeight: 72)
    }
}
