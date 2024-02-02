import SwiftUI

@available(iOS, introduced: 13, deprecated: 15, message: "Use .safeAreaInset() directly")
extension View {
    @ViewBuilder
    func safeAreaInsetBottomCompatibility<Content: View>(_ content: Content) -> some View {
        if #available(iOS 15.0, *) {
            self.safeAreaInset(
                edge: .bottom,
                alignment: .center,
                spacing: 0,
                content: { content }
            )
        } else {
            self.overlay(content, alignment: .bottom)
        }
    }
}
