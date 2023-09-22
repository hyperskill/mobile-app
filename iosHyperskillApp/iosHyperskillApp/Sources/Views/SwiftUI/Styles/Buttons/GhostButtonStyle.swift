import SwiftUI

struct GhostButtonStyle: ButtonStyle {
    private static let defaultForegroundColor = Color(ColorPalette.newButtonGhost)
    private static let pressedForegroundColor = Color(ColorPalette.newButtonGhostActive)
    private static let disabledForegroundColor = Color(ColorPalette.newButtonDisabled)

    var font = Font.body

    var minHeight: CGFloat = 44
    var maxWidth: CGFloat? = .infinity

    var alignment: Alignment = .center

    @Environment(\.isEnabled) private var isEnabled

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .foregroundColor(getForegroundColor(configuration: configuration))
            .font(font)
            .frame(maxWidth: maxWidth, minHeight: minHeight, alignment: alignment)
            .animation(.easeOut, value: configuration.isPressed)
            .animation(.easeInOut, value: isEnabled)
    }

    private func getForegroundColor(configuration: Configuration) -> Color {
        if !isEnabled {
            return Self.disabledForegroundColor
        }
        return configuration.isPressed ? Self.pressedForegroundColor : Self.defaultForegroundColor
    }
}

struct GhostButtonStyle_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            Button("Press Me", action: {})
                .buttonStyle(GhostButtonStyle())

            Button("Press Me", action: {})
                .buttonStyle(GhostButtonStyle())
                .disabled(true)
        }
    }
}
