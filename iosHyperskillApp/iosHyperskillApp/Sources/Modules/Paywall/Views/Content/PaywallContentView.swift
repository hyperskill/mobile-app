import SwiftUI

extension PaywallContentView {
    struct Appearance {
        let padding = LayoutInsets.defaultInset

        let spacing = LayoutInsets.defaultInset
        let interitemSpacing = LayoutInsets.defaultInset

        let headerImageHeight: CGFloat = 164
    }
}

struct PaywallContentView: View {
    private(set) var appearance = Appearance()

    let buyButtonText: String

    let onBuyButtonTap: () -> Void
    let onTermsOfServiceButtonTap: () -> Void

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

                Text(Strings.Paywall.title)
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
            PaywallFooterView(
                appearance: .init(spacing: appearance.interitemSpacing),
                buyButtonText: buyButtonText,
                onBuyButtonTap: onBuyButtonTap,
                onTermsOfServiceButtonTap: onTermsOfServiceButtonTap
            )
        )
    }
}

#if DEBUG
#Preview {
    PaywallContentView(
        buyButtonText: "Subscribe for $11.99/month",
        onBuyButtonTap: {},
        onTermsOfServiceButtonTap: {}
    )
}
#endif
