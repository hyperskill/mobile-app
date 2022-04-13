import SwiftUI

struct RoundedRectangleButtonStyle: ButtonStyle {
    var foregroundColor = Color.white
    var font = Font.body

    var minHeight: CGFloat = 48

    var backgroundColor = Color(ColorPalette.secondary)
    var cornerRadius: CGFloat = 8

    var bounceScale: CGFloat = 0.95
    var bounceDuration: TimeInterval = 0.15

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .foregroundColor(foregroundColor)
            .font(font)
            .frame(maxWidth: .infinity, minHeight: 48, alignment: .center)
            .background(backgroundColor.cornerRadius(cornerRadius))
            .scaleEffect(configuration.isPressed ? bounceScale : 1)
            .animation(.easeOut(duration: bounceDuration), value: configuration.isPressed)
    }
}

struct RoundedRectangleButtonStyle_Previews: PreviewProvider {
    static var previews: some View {
        Button("Press Me", action: {})
            .buttonStyle(RoundedRectangleButtonStyle())
            .padding()
    }
}
