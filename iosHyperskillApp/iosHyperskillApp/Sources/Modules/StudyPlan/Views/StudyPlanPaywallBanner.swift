import SwiftUI

extension StudyPlanPaywallBanner {
    enum Appearance {
        static let imageMaxHeight: CGFloat = 82
        static let cornerRadius: CGFloat = 8
    }
}

struct StudyPlanPaywallBanner: View {
    let action: () -> Void

    var body: some View {
        VStack(alignment: .center) {
            Image(.paywallPremiumMobile)
                .renderingMode(.original)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(maxWidth: .infinity, maxHeight: Appearance.imageMaxHeight)

            Text(Strings.Paywall.title)
                .font(.title3.bold())
                .foregroundColor(.primaryText)
                .multilineTextAlignment(.center)
                .padding(.vertical)

            Button(
                Strings.StudyPlan.paywallSubscribeButton,
                action: action
            )
            .buttonStyle(.primary)
            .shineEffect()
        }
        .padding()
        .background(Color(ColorPalette.surface))
        .cornerRadius(Appearance.cornerRadius)
    }
}

#if DEBUG
#Preview {
    VStack {
        StudyPlanPaywallBanner(action: {})
    }
    .padding()
    .background(Color.systemSecondaryBackground)
}
#endif
