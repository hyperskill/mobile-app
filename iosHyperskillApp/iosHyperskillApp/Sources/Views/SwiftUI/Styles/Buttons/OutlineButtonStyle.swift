import SwiftUI

struct OutlineButtonStyle: ButtonStyle {
    var foregroundColor = Color(ColorPalette.primary)
    var font = Font.body

    var minHeight: CGFloat = 44
    var maxWidth: CGFloat? = .infinity

    var cornerRadius: CGFloat = 8
    var borderColor = Color(ColorPalette.primaryAlpha38)
    var borderWidth: CGFloat = 1

    var bounceScale: CGFloat = 0.95
    var bounceDuration: TimeInterval = 0.15

    var alignment: Alignment = .center

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .foregroundColor(foregroundColor)
            .font(font)
            .frame(maxWidth: maxWidth, minHeight: minHeight, alignment: alignment)
            .padding(.horizontal)
            .overlay(RoundedRectangle(cornerRadius: cornerRadius).stroke(borderColor, lineWidth: borderWidth))
            .scaleEffect(configuration.isPressed ? bounceScale : 1)
            .animation(.easeOut(duration: bounceDuration), value: configuration.isPressed)
    }

    enum Style {
        case green
        case violet

        fileprivate var foregroundColor: Color {
            switch self {
            case .green:
                return Color(ColorPalette.secondary)
            case .violet:
                return Color(ColorPalette.primary)
            }
        }

        fileprivate var borderColor: Color {
            switch self {
            case .green:
                return Color(ColorPalette.secondaryAlpha38)
            case .violet:
                return Color(ColorPalette.primaryAlpha38)
            }
        }
    }
}

extension OutlineButtonStyle {
    init(style: Style) {
        self.init(foregroundColor: style.foregroundColor, borderColor: style.borderColor)
    }
}

struct OutlineButtonStyle_Previews: PreviewProvider {
    static var previews: some View {
        Button("Press Me", action: {})
            .buttonStyle(OutlineButtonStyle())
            .padding()
    }
}
