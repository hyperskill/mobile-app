import UIKit

@MainActor
enum AppAppearance {
    static func themeApplication(window: UIWindow) {
        window.tintColor = ColorPalette.primary
        KeyboardManager.setToolbarTintColor(window.tintColor)
        themeNavigationBar()
    }

    private static func themeNavigationBar() {
        let textAttributes: [NSAttributedString.Key: Any] = [.foregroundColor: UIColor.primaryText]
        UINavigationBar.appearance().titleTextAttributes = textAttributes
        UINavigationBar.appearance().largeTitleTextAttributes = textAttributes
    }
}
