import SwiftUI

/// Fixes TabView reload all content constantly, see https://developer.apple.com/forums/thread/124749
struct TabNavigationLazyView<Content: View>: View {
    private let build: () -> Content

    init(_ build: @autoclosure @escaping () -> Content) {
        self.build = build
    }

    var body: Content {
        build()
    }
}
