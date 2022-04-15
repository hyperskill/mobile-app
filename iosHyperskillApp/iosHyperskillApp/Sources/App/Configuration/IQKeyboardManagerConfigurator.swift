import Foundation
import IQKeyboardManagerSwift

enum IQKeyboardManagerConfigurator {
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
}
