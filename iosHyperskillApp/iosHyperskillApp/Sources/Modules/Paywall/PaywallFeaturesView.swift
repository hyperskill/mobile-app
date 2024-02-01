import SwiftUI

struct PaywallFeaturesView: View {
    private static let features = [
        "Access to all tracks",
        "Unlimited problems per day in the app",
        "1 hint per problem"
    ]

    var body: some View {
        GeometryReader { geometryProxy in
            VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
                ForEach(Array(Self.features.enumerated()), id: \.offset) { index, title in
                    PaywallFeatureView(
                        index: index,
                        title: title,
                        width: geometryProxy.size.width
                    )
                }
            }
        }
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

#Preview {
    PaywallFeaturesView()
}
