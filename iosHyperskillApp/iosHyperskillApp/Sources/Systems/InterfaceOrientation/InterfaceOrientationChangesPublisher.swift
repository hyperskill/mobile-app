import Foundation

enum InterfaceOrientationChangesPublisher {
    static func publishSupportedInterfaceOrientationsDidChange(
        to supportedInterfaceOrientations: UIInterfaceOrientationMask
    ) {
        NotificationCenter.default.post(
            name: .supportedInterfaceOrientationsDidChange,
            object: supportedInterfaceOrientations
        )
    }

    static func publishSupportedInterfaceOrientationsResetToDefault() {
        NotificationCenter.default.post(name: .supportedInterfaceOrientationsResetToDefault, object: nil)
    }
}

extension NSNotification.Name {
    static let supportedInterfaceOrientationsDidChange = NSNotification.Name("SupportedInterfaceOrientationsDidChange")

    static let supportedInterfaceOrientationsResetToDefault =
      NSNotification.Name("SupportedInterfaceOrientationsResetToDefault")
}
