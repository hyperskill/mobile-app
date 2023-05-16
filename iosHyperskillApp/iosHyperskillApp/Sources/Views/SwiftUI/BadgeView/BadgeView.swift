import SwiftUI

extension BadgeView {
    struct Appearance {
        let cornerRadius: CGFloat = 4

        let insets = LayoutInsets(horizontal: 8, vertical: 4)
    }
}

struct BadgeView: View {
    private(set) var appearance = Appearance()

    let text: String

    let style: Style

    var body: some View {
        Text(text)
            .font(.caption)
            .foregroundColor(style.foregroundColor)
            .padding(appearance.insets.edgeInsets)
            .background(style.backgroundColor)
            .cornerRadius(appearance.cornerRadius)
    }

    enum Style {
        case blue
        case green
        case violet

        fileprivate var foregroundColor: Color {
            switch self {
            case .blue:
                return Color(ColorPalette.primary)
            case .green:
                return Color(ColorPalette.secondary)
            case .violet:
                return Color(ColorPalette.overlayViolet)
            }
        }

        fileprivate var backgroundColor: Color {
            switch self {
            case .blue:
                return Color(ColorPalette.overlayBlueAlpha12)
            case .green:
                return Color(ColorPalette.overlayGreenAlpha12)
            case .violet:
                return Color(ColorPalette.overlayVioletAlpha12)
            }
        }
    }
}

struct BadgeView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            BadgeView(text: "Blue", style: .blue)

            BadgeView(text: "Green", style: .green)

            BadgeView(text: "Violet", style: .violet)
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
