import SwiftUI

struct OutlineButtonStyle: ButtonStyle {
    var foregroundColor = Color(ColorPalette.primary)
    var font = Font.body

    var minHeight: CGFloat = 48

    var cornerRadius: CGFloat = 8
    var borderColor = Color(ColorPalette.primary.withAlphaComponent(0.38))
    var borderWidth: CGFloat = 1

    var bounceScale: CGFloat = 0.95
    var bounceDuration: TimeInterval = 0.15

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .foregroundColor(foregroundColor)
            .font(font)
            .frame(maxWidth: .infinity, minHeight: minHeight, alignment: .center)
            .overlay(RoundedRectangle(cornerRadius: cornerRadius).stroke(borderColor, lineWidth: borderWidth))
            .scaleEffect(configuration.isPressed ? bounceScale : 1)
            .animation(.easeOut(duration: bounceDuration), value: configuration.isPressed)
    }
}

struct OutlineButtonStyle_Previews: PreviewProvider {
    static var previews: some View {
        Button("Press Me", action: {})
            .buttonStyle(OutlineButtonStyle())
            .padding()
    }
}
