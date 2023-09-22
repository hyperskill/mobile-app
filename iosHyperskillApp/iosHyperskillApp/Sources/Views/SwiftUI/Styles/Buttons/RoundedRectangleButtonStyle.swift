import SwiftUI

struct RoundedRectangleButtonStyle: ButtonStyle {
    var foregroundColor = Color.white
    var font = Font.body

    var minHeight: CGFloat = 44

    var backgroundColor = Color(ColorPalette.secondary)
    var backgroundPressedColor = Color(ColorPalette.secondary)
    var backgroundDisabledOpacity = 0.38
    var cornerRadius: CGFloat = 8

    var bounceScale: CGFloat = 0.95
    var bounceDuration: TimeInterval = 0.15

    var overlayImage: OverlayImage?

    @Environment(\.isEnabled) private var isEnabled

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .foregroundColor(foregroundColor)
            .font(font)
            .frame(maxWidth: .infinity, minHeight: minHeight, alignment: .center)
            .if(overlayImage != nil, transform: { view in
                view.overlay(
                    Image(systemName: overlayImage.require().imageSystemName)
                        .font(font)
                        .foregroundColor(foregroundColor)
                        .padding(overlayImage.require().insets)
                    ,
                    alignment: overlayImage.require().alignment
                )
            })
            .background(makeBackground(configuration: configuration))
            .scaleEffect(configuration.isPressed ? bounceScale : 1)
            .animation(.easeOut(duration: bounceDuration), value: configuration.isPressed)
    }

    private func makeBackground(configuration: Configuration) -> some View {
        (configuration.isPressed ? backgroundPressedColor : backgroundColor)
            .cornerRadius(cornerRadius)
            .opacity(isEnabled ? 1 : backgroundDisabledOpacity)
    }

    struct OverlayImage {
        let imageSystemName: String

        var insets = LayoutInsets(leading: LayoutInsets.defaultInset).edgeInsets
        let alignment = Alignment(horizontal: .leading, vertical: .center)
    }

    enum Style {
        case green
        case violet
        case newViolet

        fileprivate var foregroundColor: Color {
            switch self {
            case .green:
                return Color(ColorPalette.onSecondary)
            case .violet:
                return Color(ColorPalette.onPrimary)
            case .newViolet:
                return Color(ColorPalette.newTextOnColor)
            }
        }

        fileprivate var backgroundColor: Color {
            switch self {
            case .green:
                return Color(ColorPalette.secondary)
            case .violet:
                return Color(ColorPalette.primary)
            case .newViolet:
                return Color(ColorPalette.newButtonPrimary)
            }
        }

        fileprivate var backgroundPressedColor: Color {
            switch self {
            case .green:
                return Color(ColorPalette.secondary)
            case .violet:
                return Color(ColorPalette.primary)
            case .newViolet:
                return Color(ColorPalette.newButtonPrimaryActive)
            }
        }
    }
}

extension RoundedRectangleButtonStyle {
    init(style: Style, overlayImage: OverlayImage? = nil) {
        self.init(
            foregroundColor: style.foregroundColor,
            backgroundColor: style.backgroundColor,
            backgroundPressedColor: style.backgroundPressedColor,
            overlayImage: overlayImage
        )
    }
}

struct RoundedRectangleButtonStyle_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            Button("Press Me", action: {})
                .buttonStyle(RoundedRectangleButtonStyle())

            Button("Press Me", action: {})
                .buttonStyle(RoundedRectangleButtonStyle(overlayImage: .init(imageSystemName: "play")))

            Button("Press Me", action: {})
                .buttonStyle(RoundedRectangleButtonStyle(style: .newViolet))
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
