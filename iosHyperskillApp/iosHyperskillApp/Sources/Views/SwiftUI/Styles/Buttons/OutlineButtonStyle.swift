import SwiftUI

struct OutlineButtonStyle: ButtonStyle {
    var foregroundColor = Color(ColorPalette.primary)
    var foregroundPressedColor = Color(ColorPalette.primary)
    var font = Font.body

    var minHeight: CGFloat = 44
    var maxWidth: CGFloat? = .infinity

    var cornerRadius: CGFloat = 8
    var borderColor = Color(ColorPalette.primaryAlpha38)
    var borderPressedColor = Color(ColorPalette.primaryAlpha38)
    var borderWidth: CGFloat = 1

    var bounceScale: CGFloat = 0.95
    var bounceDuration: TimeInterval = 0.15

    var alignment: Alignment = .center
    var paddingEdgeSet = Edge.Set.horizontal

    var backgroundColor: Color = .clear

    var opacityDisabled = 0.38
    var opacityDuration: TimeInterval = 0.33

    @Environment(\.isEnabled) private var isEnabled

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .foregroundColor(configuration.isPressed ? foregroundPressedColor : foregroundColor)
            .font(font)
            .frame(maxWidth: maxWidth, minHeight: minHeight, alignment: alignment)
            .contentShape(RoundedRectangle(cornerRadius: cornerRadius)) // Increase tap area for user interaction
            .padding(paddingEdgeSet)
            .background(backgroundColor)
            .cornerRadius(cornerRadius)
            .overlay(
                RoundedRectangle(
                    cornerRadius: cornerRadius
                )
                .stroke(
                    configuration.isPressed ? borderPressedColor : borderColor,
                    lineWidth: borderWidth
                )
            )
            .opacity(isEnabled ? 1 : opacityDisabled)
            .scaleEffect(configuration.isPressed ? bounceScale : 1)
            .animation(.easeOut(duration: bounceDuration), value: configuration.isPressed)
            .animation(.easeInOut(duration: opacityDuration), value: isEnabled)
    }

    enum Style {
        case green
        case violet
        case newGray

        fileprivate var foregroundColor: Color {
            switch self {
            case .green:
                Color(ColorPalette.secondary)
            case .violet:
                Color(ColorPalette.primary)
            case .newGray:
                Color(ColorPalette.newButtonTertiary)
            }
        }

        fileprivate var foregroundPressedColor: Color {
            switch self {
            case .green:
                Color(ColorPalette.secondary)
            case .violet:
                Color(ColorPalette.primary)
            case .newGray:
                Color(ColorPalette.newButtonTertiaryActive)
            }
        }

        fileprivate var borderColor: Color {
            switch self {
            case .green:
                Color(ColorPalette.secondaryAlpha38)
            case .violet:
                Color(ColorPalette.primaryAlpha38)
            case .newGray:
                Color(ColorPalette.newButtonTertiary)
            }
        }

        fileprivate var borderPressedColor: Color {
            switch self {
            case .green:
                Color(ColorPalette.secondaryAlpha38)
            case .violet:
                Color(ColorPalette.primaryAlpha38)
            case .newGray:
                Color(ColorPalette.newButtonTertiaryActive)
            }
        }
    }
}

extension OutlineButtonStyle {
    init(style: Style) {
        self.init(
            foregroundColor: style.foregroundColor,
            foregroundPressedColor: style.foregroundPressedColor,
            borderColor: style.borderColor,
            borderPressedColor: style.borderPressedColor
        )
    }
}

extension ButtonStyle where Self == OutlineButtonStyle {
    static var tertiary: OutlineButtonStyle {
        OutlineButtonStyle(style: .newGray)
    }
}

#Preview {
    VStack {
        Button("Press Me", action: {})
            .buttonStyle(OutlineButtonStyle())

        Button("Press Me", action: {})
            .buttonStyle(OutlineButtonStyle(style: .newGray))

        Button("Press Me", action: {})
            .buttonStyle(.tertiary)
    }
    .padding()
}
