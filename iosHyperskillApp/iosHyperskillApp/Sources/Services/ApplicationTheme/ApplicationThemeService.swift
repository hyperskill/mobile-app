import UIKit

protocol ApplicationThemeServiceProtocol: AnyObject {
    var theme: ApplicationTheme { get }
}

final class ApplicationThemeService: ApplicationThemeServiceProtocol {
    var theme: ApplicationTheme {
        switch UITraitCollection.current.userInterfaceStyle {
        case .dark:
            return .dark
        default:
            return .light
        }
    }
}
