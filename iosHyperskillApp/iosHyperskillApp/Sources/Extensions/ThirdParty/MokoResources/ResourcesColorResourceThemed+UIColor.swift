import shared
import UIKit

extension ColorDesc {
    var uiColor: UIColor {
        switch self {
        case let descResource as ColorDescResource:
            return descResource.resource.getUIColor()
        case let singleColor as ColorDescSingle:
            return singleColor.color.uiColor
        case let themedColor as ColorDescThemed:
            return themedColor.dynamicUIColor
        default:
            return UIColor(hex6: 0xffffff)
        }
    }
}

extension ColorDescThemed {
    var dynamicUIColor: UIColor {
        UIColor.dynamic(light: self.lightColor.uiColor, dark: self.darkColor.uiColor)
    }
}

private struct ColorHandlingError : LocalizedError {
    let description: String

    init(_ description: String) {
        self.description = description
    }

    var errorDescription: String? {
        description
    }
}
