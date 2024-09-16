import shared
import SwiftUI

extension PaywallSubscriptionProductsView {
    struct Appearance {
        var spacing = LayoutInsets.smallInset

        let padding = LayoutInsets.defaultInset

        let badgeInsets = LayoutInsets(horizontal: 8, vertical: 4)
        let badgeFont = UIFont.preferredFont(forTextStyle: .footnote)

        func badgeTopOffset() -> CGFloat {
            badgeFont.pointSize / 2.0 + badgeInsets.top
        }
    }
}

struct PaywallSubscriptionProductsView: View {
    private(set) var appearance = Appearance()

    let subscriptionProducts: [PaywallFeatureViewStateContentSubscriptionProduct]

    let onTap: (PaywallFeatureViewStateContentSubscriptionProduct) -> Void

    var body: some View {
        VStack(alignment: .center, spacing: appearance.spacing) {
            ForEach(
                Array(subscriptionProducts.enumerated()),
                id: \.element.productId
            ) { index, product in
                buildProductView(
                    product: product,
                    action: {
                        onTap(product)
                    }
                )
                .padding(.top, product.isBestValue && index > 0 ? appearance.spacing : 0)
            }
        }
    }

    private var bestValueBadgeView: some View {
        Text(Strings.Paywall.bestValueBadge)
            .font(Font(appearance.badgeFont))
            .foregroundColor(Color(ColorPalette.onPrimary))
            .padding(appearance.badgeInsets.edgeInsets)
            .background(Color(ColorPalette.primary))
            .clipShape(Capsule())
            .fixedSize()
    }

    private func buildProductView(
        product: PaywallFeatureViewStateContentSubscriptionProduct,
        action: @escaping () -> Void
    ) -> some View {
        Button(
            action: action,
            label: {
                HStack(alignment: .center, spacing: 0) {
                    Text(product.title)
                        .font(.body.bold())

                    Spacer()

                    Text(product.subtitle)
                        .font(.body)
                }
                .foregroundColor(.newPrimaryText)
                .padding(.horizontal, appearance.padding)
                .padding(.vertical, product.isSelected ? appearance.padding * 2 : appearance.padding)
                .conditionalOpacity(isEnabled: product.isSelected)
                .addBorder(
                    color: product.isSelected ? Color(ColorPalette.primary) : .border,
                    width: product.isSelected ? 2 : 1
                )
                .animation(.default, value: product.isSelected)
                .overlay(
                    bestValueBadgeView
                        .opacity(product.isBestValue ? 1 : 0)
                        .alignmentGuide(.top, computeValue: { dimension in
                            dimension[.top] + appearance.badgeTopOffset()
                        })
                        .alignmentGuide(.trailing, computeValue: { dimension in
                            dimension[.trailing] - appearance.badgeInsets.trailing
                        })
                    ,
                    alignment: .init(horizontal: .trailing, vertical: .top)
                )
            }
        )
    }
}

#if DEBUG
#Preview {
    VStack {
        PaywallSubscriptionProductsView(
            subscriptionProducts: [
                .init(
                    productId: "1",
                    title: "Monthly Subscription",
                    subtitle: "$11.99 / month",
                    isBestValue: false,
                    isSelected: false
                ),
                .init(
                    productId: "2",
                    title: "Yearly Subscription",
                    subtitle: "$99.99 / year",
                    isBestValue: true,
                    isSelected: true
                )
            ],
            onTap: { _ in }
        )
    }
    .padding()
}
#endif
