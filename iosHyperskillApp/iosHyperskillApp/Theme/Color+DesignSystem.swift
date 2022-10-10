import SwiftUI
import UIKit

extension Color {
    // MARK: - UI Element Colors -

    // MARK: Text

    /// The material color for text labels that contain primary content.
    ///
    /// `OnSurface_0.87`
    static var primaryText: Color { Color(UIColor.primaryText) }

    /// The material color for text labels that contain secondary content.
    ///
    /// `OnSurface_0.6`
    static var secondaryText: Color { Color(UIColor.secondaryText) }

    /// The material color for text labels that contain disabled content.
    ///
    /// `OnSurface_0.38`
    static var disabledText: Color { Color(UIColor.disabledText) }

    /// The system color for text labels that contain primary content.
    static var systemPrimaryText: Color { Color(UIColor.label) }

    /// The system color for text labels that contain secondary content.
    static var systemSecondaryText: Color { Color(UIColor.secondaryLabel) }

    /// The system color for text labels that contain tertiary content.
    static var systemTertiaryText: Color { Color(UIColor.tertiaryLabel) }

    /// The system color for text labels that contain quaternary content.
    static var systemQuaternaryText: Color { Color(UIColor.quaternaryLabel) }

    /// The system color for placeholder text in controls or text views.
    static var systemPlaceholderText: Color { Color(UIColor.placeholderText) }

    // MARK: Separator

    /// The color for thin borders or divider lines that allows some underlying content to be visible.
    static var separator: Color { Color(UIColor.separator) }

    /// The color for borders or divider lines that hides any underlying content.
    static var opaqueSeparator: Color { Color(UIColor.opaqueSeparator) }

    /// The color for borders.
    static var border: Color { Color(ColorPalette.onSurfaceAlpha12) }

    // MARK: Standard Content Background Colors

    /// The color for the main background of the interface.
    static var background: Color { Color(ColorPalette.background) }

    /// The color for the main background of the interface.
    static var systemBackground: Color { Color(UIColor.systemBackground) }

    /// The color for content layered on top of the main background.
    static var systemSecondaryBackground: Color { Color(UIColor.secondarySystemBackground) }

    /// The color for content layered on top of secondary backgrounds.
    static var systemTertiaryBackground: Color { Color(UIColor.tertiarySystemBackground) }

    // MARK: Grouped Content Background Colors

    /// The color for the main background of grouped interface.
    static var systemGroupedBackground: Color { Color(UIColor.systemGroupedBackground) }

    /// The color for content layered on top of the main background of grouped interface.
    static var systemSecondaryGroupedBackground: Color { Color(UIColor.secondarySystemGroupedBackground) }

    /// The color for content layered on top of secondary backgrounds of grouped interface.
    static var systemTertiaryGroupedBackground: Color { Color(UIColor.tertiarySystemGroupedBackground) }
}
