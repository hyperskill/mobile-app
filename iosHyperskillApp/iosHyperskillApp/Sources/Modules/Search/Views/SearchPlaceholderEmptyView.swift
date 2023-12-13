import SwiftUI

struct SearchPlaceholderEmptyView: View {
    var body: some View {
        VStack {
            Text(Strings.Search.placeholderEmptyTitle)
                .font(.title2.bold())
                .foregroundColor(.primaryText)

            Text(Strings.Search.placeholderEmptySubtitle)
                .font(.body)
                .foregroundColor(.secondaryText)
        }
        .frame(maxWidth: .infinity)
        .multilineTextAlignment(.center)
    }
}

#Preview {
    SearchPlaceholderEmptyView()
        .padding()
}
