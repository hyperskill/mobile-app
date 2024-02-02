import SwiftUI

extension PaywallView {
    struct Appearance {
        let padding = LayoutInsets.defaultInset

        let spacing = LayoutInsets.defaultInset
        let interitemSpacing = LayoutInsets.smallInset

        let headerImageHeight: CGFloat = 164
    }
}

struct PaywallView: View {
    private(set) var appearance = Appearance()

    @State private var scrollOffset = CGPoint()

    var body: some View {
        OffsetObservingScrollView(
            axes: .vertical,
            showsIndicators: false,
            offset: $scrollOffset
        ) {
            VStack(alignment: .leading, spacing: appearance.spacing) {
                Image(.paywallPremiumMobile)
                    .renderingMode(.original)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(maxWidth: .infinity)
                    .frame(
                        height: scrollOffset.y < 0
                          ? appearance.headerImageHeight + abs(scrollOffset.y)
                          : appearance.headerImageHeight
                    )
                    .offset(y: scrollOffset.y < 0 ? scrollOffset.y : 0)
                    .padding(.horizontal, -appearance.padding)

                Text("Solve unlimited problems with Mobile only plan")
                    .font(.largeTitle.bold())
                    .foregroundColor(.newPrimaryText)
                    .padding(.vertical)

                PaywallFeaturesView(
                    appearance: .init(spacing: appearance.interitemSpacing)
                )
            }
            .padding(appearance.padding)
        }
        .safeAreaInsetBottomCompatibility(
            PaywallFooterView(appearance: .init(spacing: appearance.interitemSpacing))
        )
    }
}

#Preview {
    PaywallView()
}
