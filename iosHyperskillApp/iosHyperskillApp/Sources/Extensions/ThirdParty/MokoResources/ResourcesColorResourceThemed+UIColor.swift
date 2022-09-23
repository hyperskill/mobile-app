import shared
import UIKit

extension ResourcesColorResource.Themed {
    var dynamicUIColor: UIColor {
        UIColor.dynamic(light: self.light.uiColor, dark: self.dark.uiColor)
    }
}
