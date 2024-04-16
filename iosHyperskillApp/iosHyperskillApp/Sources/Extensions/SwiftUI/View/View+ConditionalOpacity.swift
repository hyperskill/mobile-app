import SwiftUI

extension View {
    /// Modifies the opacity of the view based on a condition and applies an animation to the opacity change.
    /// - Parameters:
    ///   - isEnabled: The condition based on which the opacity is set.
    ///   - opacityEnabled: The opacity when the `isEnabled` is `true`.
    ///   - opacityDisabled: The opacity when the `isEnabled` is `false`.
    ///   - animation: The animation applied to the opacity change.
    func conditionalOpacity(
        isEnabled: Bool,
        opacityEnabled: Double = 1.0,
        opacityDisabled: Double = 0.5,
        animation: Animation? = .easeInOut(duration: 0.33)
    ) -> some View {
        self
            .opacity(isEnabled ? opacityEnabled : opacityDisabled)
            .animation(animation, value: isEnabled)
    }
}
