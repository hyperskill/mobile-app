import SwiftUI

struct PrimaryTextButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .foregroundColor(Color(ColorPalette.primary))
            .font(.subheadline)
            .opacity(configuration.isPressed ? 0.5 : 1)
    }
}

struct PrimaryTextButtonStyle_Previews: PreviewProvider {
    static var previews: some View {
        Button("Press Me", action: {})
            .buttonStyle(PrimaryTextButtonStyle())
            .padding()
    }
}
