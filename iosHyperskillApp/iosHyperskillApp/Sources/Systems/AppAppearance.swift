import UIKit

enum AppAppearance {
    static func themeApplication() {
        self.themeNavigationBar()
    }

    private static func themeNavigationBar() {
        let coloredAppearance = UINavigationBarAppearance()
        coloredAppearance.configureWithOpaqueBackground()

        coloredAppearance.backgroundColor = ColorPalette.background

        coloredAppearance.titleTextAttributes = [.foregroundColor: ColorPalette.onSurfaceAlpha87]
        coloredAppearance.largeTitleTextAttributes = [.foregroundColor: ColorPalette.onSurfaceAlpha87]

        UINavigationBar.appearance().standardAppearance = coloredAppearance
        UINavigationBar.appearance().compactAppearance = coloredAppearance
        UINavigationBar.appearance().scrollEdgeAppearance = coloredAppearance

        UIBarButtonItem.appearance().tintColor = ColorPalette.onSurface.withAlphaComponent(0.6)
    }
}
