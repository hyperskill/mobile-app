import SwiftUI

extension View {
    /// This modifier is used to hide views.
    /// Views that are hidden are invisible and don’t receive or respond to the interactions but they remain in view hierarchy and take the space in layout.
    @ViewBuilder
    func hidden(_ condition: @autoclosure () -> Bool) -> some View {
        if condition() {
            self.hidden()
        } else {
            self
        }
    }
}
