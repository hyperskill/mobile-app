import SwiftUI

extension View {
    @ViewBuilder
    func hidden(_ condition: @autoclosure () -> Bool) -> some View {
        if condition() {
            self.hidden()
        } else {
            self
        }
    }
}
