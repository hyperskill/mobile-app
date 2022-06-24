import UIKit

protocol ApplicationThemeServiceProtocol: AnyObject {
    var theme: ApplicationTheme { get }
}

final class ApplicationThemeService: ApplicationThemeServiceProtocol {
    private let traitCollection: UITraitCollection

    init(traitCollection: UITraitCollection = .current) {
        self.traitCollection = traitCollection
    }

    var theme: ApplicationTheme {
        switch traitCollection.userInterfaceStyle {
        case .dark:
            return .dark
        default:
            return .light
        }
    }
}
