import SwiftUI

extension TrackSelectionDetailsProvidersView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
        let interitemSpacing = LayoutInsets.smallInset
    }
}

struct TrackSelectionDetailsProvidersView: View {
    private(set) var appearance = Appearance()

    let mainProviderTitle: String?
    let mainProviderDescription: String?

    let otherProvidersDescription: String?

    var body: some View {
        if mainProviderTitle?.isEmpty ?? true {
            EmptyView()
        } else {
            CardView {
                VStack(alignment: .leading, spacing: appearance.spacing) {
                    buildMainProviderView()
                    buildOtherProvidersView()
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
        }
    }

    @ViewBuilder
    private func buildMainProviderView() -> some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            VStack(alignment: .leading, spacing: appearance.interitemSpacing) {
                Text(Strings.TrackSelectionDetails.mainProviderTitle)
                    .font(.subheadline)

                Text(mainProviderTitle)
                    .font(.headline)
            }

            if let mainProviderDescription, !mainProviderDescription.isEmpty {
                Text(mainProviderDescription)
                    .font(.body)
            }
        }
        .foregroundColor(.primaryText)
    }

    @ViewBuilder
    private func buildOtherProvidersView() -> some View {
        if let otherProvidersDescription, !otherProvidersDescription.isEmpty {
            VStack(alignment: .leading, spacing: appearance.spacing) {
                Divider()

                VStack(alignment: .leading, spacing: appearance.interitemSpacing) {
                    Text(Strings.TrackSelectionDetails.otherProvidersTitle)
                        .font(.subheadline)
                        .foregroundColor(.secondaryText)

                    Text(otherProvidersDescription)
                        .font(.body)
                        .foregroundColor(.primaryText)
                }
            }
        }
    }
}

#if DEBUG
struct TrackSelectionDetailsProvidersView_Previews: PreviewProvider {
    static var previews: some View {
        TrackSelectionDetailsProvidersView(
            mainProviderTitle: "JetBrains Academy",
            mainProviderDescription: """
Learn to program by creating real-world applications. \
Empowered by a personalized study plan, interactive projects, and integration with JetBrains IDEs, \
you’ll gain hands-on programming experience that is essential for your career as a developer.
""",
            otherProvidersDescription: "Edvancium, Basic"
        )
        .padding()
        .background(Color.systemGroupedBackground)
    }
}
#endif
