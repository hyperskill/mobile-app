import SwiftUI

extension PaywallFeaturesView {
    struct Appearance {
        var spacing = LayoutInsets.smallInset
    }
}

struct PaywallFeaturesView: View {
    private static let features = [
        Strings.Paywall.subscriptionFeature1,
        Strings.Paywall.subscriptionFeature2,
        Strings.Paywall.subscriptionFeature3,
        Strings.Paywall.subscriptionFeature4
    ]

    private(set) var appearance = Appearance()

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            ForEach(Array(Self.features.enumerated()), id: \.offset) { index, title in
                PaywallFeatureView(
                    index: index,
                    title: title,
                    width: UIScreen.main.bounds.width
                )
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
    }
}

private struct PaywallFeatureView: View {
    var index: Int
    var title: String

    var width: CGFloat

    @State private var animateTitle = false
    @State private var animateIcon = false

    var body: some View {
        Label(
            title: {
                Text(title)
                    .foregroundColor(.newPrimaryText)
                    .offset(x: !animateTitle ? -width : 0)
                    .clipped()
            },
            icon: {
                if animateIcon {
                    Image(systemName: "checkmark")
                        .renderingMode(.template)
                        .foregroundColor(Color(ColorPalette.primary))
                        .transition(.scale)
                }
            }
        )
        .font(.body)
        .taskCompatibility {
            guard !animateIcon else {
                return
            }
            // Using the index value to calculate the delay value.
            let iconAnimationDelay = UnitConverters.Nanosecond.from(
                second: Double(index) * Animation.iconAnimationDelay
            )
            try? await Task.sleep(nanoseconds: iconAnimationDelay)
            withAnimation(.snappy(duration: Animation.iconAnimationDuration)) {
                animateIcon = true
            }

            try? await Task.sleep(
                nanoseconds: UnitConverters.Nanosecond.from(second: Animation.titleAnimationDelay)
            )
            withAnimation(.easeInOut(duration: Animation.titleAnimationDuration)) {
                animateTitle = true
            }
        }
    }

    enum Animation {
        static let iconAnimationDelay: TimeInterval = 0.4
        static let iconAnimationDuration: TimeInterval = 0.33

        static let titleAnimationDelay: TimeInterval = 0.1
        static let titleAnimationDuration: TimeInterval = 0.33
    }
}

#if DEBUG
#Preview {
    PaywallFeaturesView()
}
#endif
