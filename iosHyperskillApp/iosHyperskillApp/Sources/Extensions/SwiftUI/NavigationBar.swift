import SwiftUI

extension UINavigationController {
    override open func viewWillLayoutSubviews() {
        navigationBar.topItem?.backButtonDisplayMode = .minimal
        navigationBar.tintColor = ColorPalette.onSurfaceAlpha60
    }
}
