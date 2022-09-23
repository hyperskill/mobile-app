import shared
import UIKit

protocol ApplicationThemeServiceProtocol: AnyObject {
    var currentTheme: ApplicationTheme { get }

    func applyDefaultTheme()
    func apply(newTheme: ApplicationTheme)
}

final class ApplicationThemeService: ApplicationThemeServiceProtocol {
    private let profileSettingsInteractor: ProfileSettingsInteractor

    var currentTheme: ApplicationTheme {
        let profileSettings = profileSettingsInteractor.getProfileSettings()
        return ApplicationTheme(sharedTheme: profileSettings.theme)
    }

    init(profileSettingsInteractor: ProfileSettingsInteractor) {
        self.profileSettingsInteractor = profileSettingsInteractor
    }

    func applyDefaultTheme() {
        apply(newTheme: currentTheme)
    }

    func apply(newTheme: ApplicationTheme) {
        DispatchQueue.main.async {
            let application = UIApplication.shared
            let userInterfaceStyle = newTheme.userInterfaceStyle

            if application.supportsMultipleScenes {
                for connectedScene in application.connectedScenes {
                    if let windowScene = connectedScene as? UIWindowScene {
                        for window in windowScene.windows {
                            window.overrideUserInterfaceStyle = userInterfaceStyle
                        }
                    }
                }
            } else {
                for window in application.windows {
                    window.overrideUserInterfaceStyle = userInterfaceStyle
                }
            }
        }
    }
}

extension ApplicationThemeService {
    static var `default`: ApplicationThemeService {
        let profileSettingsComponent = AppGraphBridge.sharedAppGraph.buildProfileSettingsComponent()
        return ApplicationThemeService(profileSettingsInteractor: profileSettingsComponent.profileSettingsInteractor)
    }
}

private extension ApplicationTheme {
    var userInterfaceStyle: UIUserInterfaceStyle {
        switch self {
        case .system:
            return .unspecified
        case .dark:
            return .dark
        case .light:
            return .light
        }
    }
}
