import SwiftUI

struct BounceButtonStyle: ButtonStyle {
    var bounceScale: CGFloat = 0.95
    var bounceDuration: TimeInterval = 0.15

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .scaleEffect(configuration.isPressed ? bounceScale : 1)
            .animation(.easeOut(duration: bounceDuration), value: configuration.isPressed)
    }
}

struct BounceButtonStyle_Previews: PreviewProvider {
    static var previews: some View {
        Button("Press Me", action: {})
            .buttonStyle(BounceButtonStyle())
            .padding()
    }
}
