import SwiftUI

struct SearchPlaceholderSuggestionsView: View {
    var body: some View {
        VStack {
            Text(Strings.Search.placeholderSuggestionsTitle)
                .font(.title2.bold())
                .foregroundColor(.primaryText)

            Text(Strings.Search.placeholderSuggestionsSubtitle)
                .font(.body)
                .foregroundColor(.secondaryText)
        }
        .frame(maxWidth: .infinity)
        .multilineTextAlignment(.center)
    }
}

#Preview {
    SearchPlaceholderSuggestionsView()
        .padding()
}
