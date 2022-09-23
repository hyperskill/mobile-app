import UIKit

protocol CodeEditorThemeServiceProtocol: AnyObject {
    var theme: CodeEditorTheme { get }
}

final class CodeEditorThemeService: CodeEditorThemeServiceProtocol {
    private static let defaultDarkModeThemeName = "vs2015"
    private static let defaultLightModeThemeName = "vs"

    private let deviceInfo: DeviceInfo
    private let applicationThemeService: ApplicationThemeServiceProtocol
    private let userDefaults: UserDefaults

    var theme: CodeEditorTheme {
        CodeEditorTheme(font: font, name: themeName)
    }

    private var font: UIFont {
        let codeElementsSize: CodeQuizElementsSize = DeviceInfo.current.isPad ? .big : .small
        let fontSize = codeElementsSize.elements.editor.realSizes.fontSize
        return UIFont(name: "Courier", size: fontSize) ?? .systemFont(ofSize: fontSize)
    }

    private var themeName: String {
        switch applicationThemeService.currentTheme {
        case .light:
            return lightModeThemeName
        case .dark:
            return darkModeThemeName
        case .system:
            switch UITraitCollection.current.userInterfaceStyle {
            case .dark:
                return darkModeThemeName
            default:
                return lightModeThemeName
            }
        }
    }

    private var lightModeThemeName: String {
        get {
            if let themeName = userDefaults.string(forKey: Key.lightModeThemeName.rawValue) {
                return themeName
            } else {
                self.lightModeThemeName = Self.defaultLightModeThemeName
                return Self.defaultLightModeThemeName
            }
        }
        set {
            userDefaults.setValue(newValue, forKey: Key.lightModeThemeName.rawValue)
        }
    }

    private var darkModeThemeName: String {
        get {
            if let themeName = userDefaults.string(forKey: Key.darkModeThemeName.rawValue) {
                return themeName
            } else {
                self.darkModeThemeName = Self.defaultDarkModeThemeName
                return Self.defaultDarkModeThemeName
            }
        }
        set {
            userDefaults.setValue(newValue, forKey: Key.darkModeThemeName.rawValue)
        }
    }

    init(
        deviceInfo: DeviceInfo = .current,
        applicationThemeService: ApplicationThemeServiceProtocol = ApplicationThemeService.default,
        userDefaults: UserDefaults = .standard
    ) {
        self.deviceInfo = deviceInfo
        self.applicationThemeService = applicationThemeService
        self.userDefaults = userDefaults
    }

    private enum Key: String {
        case darkModeThemeName = "CodeEditorDarkModeThemeName"
        case lightModeThemeName = "CodeEditorLightModeThemeName"
    }
}
