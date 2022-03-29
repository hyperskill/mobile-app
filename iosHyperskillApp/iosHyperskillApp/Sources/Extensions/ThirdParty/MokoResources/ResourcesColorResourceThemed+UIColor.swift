import shared
import UIKit

extension ResourcesColorResource.Themed {
    var dynamicUIColor: UIColor {
        UIColor { (traitCollection: UITraitCollection) -> UIColor in
            switch traitCollection.userInterfaceStyle {
            case .light:
                return self.light.uiColor
            case .dark:
                return self.dark.uiColor
            default:
                return self.light.uiColor
            }
        }
    }
}
