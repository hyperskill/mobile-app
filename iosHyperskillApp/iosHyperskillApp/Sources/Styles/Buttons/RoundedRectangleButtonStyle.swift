import SwiftUI

struct RoundedRectangleButtonStyle: ButtonStyle {
    var foregroundColor = Color.white
    var font = Font.body

    var minHeight: CGFloat = 44

    var backgroundColor = Color(ColorPalette.secondary)
    var cornerRadius: CGFloat = 8

    var bounceScale: CGFloat = 0.95
    var bounceDuration: TimeInterval = 0.15

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .foregroundColor(foregroundColor)
            .font(font)
            .frame(maxWidth: .infinity, minHeight: minHeight, alignment: .center)
            .background(backgroundColor.cornerRadius(cornerRadius))
            .scaleEffect(configuration.isPressed ? bounceScale : 1)
            .animation(.easeOut(duration: bounceDuration), value: configuration.isPressed)
    }

    enum Style {
        case green
        case violet

        fileprivate var foregroundColor: Color {
            switch self {
            case .green:
                return Color(ColorPalette.onSecondary)
            case .violet:
                return Color(ColorPalette.onPrimary)
            }
        }

        fileprivate var backgroundColor: Color {
            switch self {
            case .green:
                return Color(ColorPalette.secondary)
            case .violet:
                return Color(ColorPalette.primary)
            }
        }
    }
}

extension RoundedRectangleButtonStyle {
    init(style: Style) {
        self.init(foregroundColor: style.foregroundColor, backgroundColor: style.backgroundColor)
    }
}

struct RoundedRectangleButtonStyle_Previews: PreviewProvider {
    static var previews: some View {
        Button("Press Me", action: {})
            .buttonStyle(RoundedRectangleButtonStyle())
            .padding()
    }
}
