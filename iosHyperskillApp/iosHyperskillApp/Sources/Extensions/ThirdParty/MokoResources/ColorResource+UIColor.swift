import shared
import UIKit

extension shared.ColorResource {
    var uiColor: UIColor {
        switch self.asColorDesc() {
        case let descResource as ColorDescResource:
            descResource.resource.getUIColor() // Color from the named asset
        case let singleColor as ColorDescSingle:
            singleColor.color.uiColor
        case let themedColor as ColorDescThemed:
            UIColor.dynamic(
                light: themedColor.lightColor.uiColor,
                dark: themedColor.darkColor.uiColor
            )
        default:
            fatalError("Unknown color description for name = \(self.name)")
        }
    }
}
