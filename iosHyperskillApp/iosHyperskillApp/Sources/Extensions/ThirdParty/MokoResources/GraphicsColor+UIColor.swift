import shared
import UIKit

extension GraphicsColor {
    var uiColor: UIColor {
        UIColor(
            red: CGFloat(self.red) / 255,
            green: CGFloat(self.green) / 255,
            blue: CGFloat(self.blue) / 255,
            alpha: CGFloat(self.alpha) / 255
        )
    }
}
