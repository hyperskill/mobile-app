import shared
import SwiftUI

struct StepQuizCodeBlanksSuggestionsView: View {
    let suggestions: [Suggestion]

    let onSuggestionTap: (Suggestion) -> Void

    var body: some View {
        let views = ForEach(suggestions, id: \.self) { suggestion in
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

        if #available(iOS 16.0, *) {
            FlowLayout(spacing: LayoutInsets.defaultInset) {
                views
            }
            .padding(LayoutInsets.defaultInset)
            .frame(minHeight: 72)
        } else {
            VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
                views
            }
            .padding(LayoutInsets.defaultInset)
            .frame(minHeight: 72)
        }
    }
}
