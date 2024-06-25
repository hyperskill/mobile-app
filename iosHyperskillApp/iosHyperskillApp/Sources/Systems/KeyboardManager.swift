import Foundation
import IQKeyboardManagerSwift

@MainActor
enum KeyboardManager {
    static let defaultKeyboardDistanceFromTextField: CGFloat = 24

    static func configure() {
        IQKeyboardManager.shared.enable = true
        IQKeyboardManager.shared.shouldResignOnTouchOutside = true
        IQKeyboardManager.shared.keyboardDistanceFromTextField = self.defaultKeyboardDistanceFromTextField
        IQKeyboardManager.shared.enableAutoToolbar = false
    }

    static func setKeyboardDistanceFromTextField(_ distance: CGFloat) {
        IQKeyboardManager.shared.keyboardDistanceFromTextField = distance
    }

    static func setDefaultKeyboardDistanceFromTextField() {
        self.setKeyboardDistanceFromTextField(self.defaultKeyboardDistanceFromTextField)
    }

    static func setEnabled(_ isEnabled: Bool) {
        IQKeyboardManager.shared.enable = isEnabled
    }

    static func setEnableAutoToolbar(_ enableAutoToolbar: Bool) {
        IQKeyboardManager.shared.enableAutoToolbar = enableAutoToolbar
    }

    static func setToolbarTintColor(_ toolbarTintColor: UIColor?) {
        IQKeyboardManager.shared.toolbarTintColor = toolbarTintColor
    }

    static func reloadLayoutIfNeeded() {
        IQKeyboardManager.shared.reloadLayoutIfNeeded()
    }
}
