import Foundation
import shared

extension ApplicationTheme {
    var sharedTheme: Theme {
        switch self {
        case .light:
            return Theme.light
        case .dark:
            return Theme.dark
        case .system:
            return Theme.system
        }
    }

    init(sharedTheme: Theme) {
        switch sharedTheme {
        case Theme.light:
            self = .light
        case Theme.dark:
            self = .dark
        case Theme.system:
            self = .system
        default:
            fatalError("unsupported theme type")
        }
    }
}
