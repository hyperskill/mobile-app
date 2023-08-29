import shared
import UIKit

extension BadgeRank {
    var foregroundColor: UIColor {
        switch self {
        case .apprentice, .expert:
            return ColorPalette.overlayBlueBrand
        case .master:
            return ColorPalette.overlayBlue
        case .legendary:
            return .primaryText
        case .locked, .unknown:
            return .disabledText
        default:
            return .disabledText
        }
    }
}
