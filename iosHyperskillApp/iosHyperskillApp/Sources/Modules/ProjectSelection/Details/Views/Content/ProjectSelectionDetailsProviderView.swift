import SwiftUI

struct ProjectSelectionDetailsProviderView: View {
    let title: String?

    var body: some View {
        if let title, !title.isEmpty {
            CardView {
                VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
                    Text(Strings.ProjectSelectionDetails.providedByTitle)
                        .font(.subheadline)

                    Text(title)
                        .font(.headline)
                }
                .foregroundColor(.primaryText)
                .frame(maxWidth: .infinity, alignment: .leading)
            }
        } else {
            EmptyView()
        }
    }
}

#if DEBUG
struct ProjectSelectionDetailsProviderView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectSelectionDetailsProviderView(
            title: "JetBrains Academy"
        )
        .padding()
        .background(Color.systemGroupedBackground)
    }
}
#endif
