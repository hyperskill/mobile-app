import SwiftUI

extension PaywallView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
        let interitemSpacing = LayoutInsets.smallInset

        let illustrationMaxHeight: CGFloat = 164
    }
}

struct PaywallView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        ScrollView(.vertical, showsIndicators: false) {
            VStack(alignment: .leading, spacing: appearance.spacing) {
                Image(.paywallPremiumMobile)
                    .renderingMode(.original)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(
                        maxWidth: .infinity,
                        maxHeight: appearance.illustrationMaxHeight
                    )

                Text("Solve unlimited problems with Mobile only plan")
                    .font(.largeTitle.bold())
                    .foregroundColor(.newPrimaryText)
                    .padding(.vertical)

                PaywallFeaturesView(
                    appearance: .init(spacing: appearance.interitemSpacing)
                )
            }
            .padding()
        }
        .scrollBounceBehaviorBasedOnSize()
        .safeAreaInsetBottomCompatibility(
            PaywallFooterView(appearance: .init(spacing: appearance.interitemSpacing))
        )
    }
}

#Preview {
    PaywallView()
}
