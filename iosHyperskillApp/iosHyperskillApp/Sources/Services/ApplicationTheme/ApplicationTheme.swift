import Foundation

enum ApplicationTheme: String, CaseIterable, Identifiable {
    case light
    case dark
    case system

    var id: Self { self }

    var title: String {
        switch self {
        case .light:
            return Strings.Settings.Theme.light
        case .dark:
            return Strings.Settings.Theme.dark
        case .system:
            return Strings.Settings.Theme.system
        }
    }
}
