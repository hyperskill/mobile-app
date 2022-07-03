import UIKit

extension UIView {
    var isDarkInterfaceStyle: Bool {
        traitCollection.userInterfaceStyle == .dark
    }

    func performBlockIfAppearanceChanged(from previousTraits: UITraitCollection?, block: () -> Void) {
        if traitCollection.hasDifferentColorAppearance(comparedTo: previousTraits) {
            block()
        }
    }

    func performBlockUsingViewTraitCollection(_ block: () -> Void) {
        // Execute the block directly if the traits are the same.
        if traitCollection.containsTraits(in: .current) {
            block()
        } else {
            self.traitCollection.performAsCurrent {
                block()
            }
        }
    }
}
