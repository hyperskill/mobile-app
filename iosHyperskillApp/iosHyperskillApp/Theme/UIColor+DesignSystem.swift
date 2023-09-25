import UIKit

extension UIColor {
    // MARK: Text

    /// The material color for text labels that contain primary content.
    ///
    /// `OnSurface_0.87`
    static var primaryText: UIColor { ColorPalette.onSurfaceAlpha87 }

    /// The material color for text labels that contain secondary content.
    ///
    /// `OnSurface_0.6`
    static var secondaryText: UIColor { ColorPalette.onSurfaceAlpha60 }

    /// The material color for text labels that contain tertiary content.
    ///
    /// `OnSurface_0.38`
    static var tertiaryText: UIColor { ColorPalette.onSurfaceAlpha38 }

    /// The new color for text labels that contain primary content.
    static var newPrimaryText: UIColor { ColorPalette.newTextPrimary }

    /// The new color for text labels that contain secondary content.
    static var newSecondaryText: UIColor { ColorPalette.newTextSecondary }
}
