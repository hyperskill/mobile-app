import Foundation
import UIKit

final class InterfaceOrientationManager {
    private(set) var supportedInterfaceOrientations: UIInterfaceOrientationMask
    private let defaultSupportedInterfaceOrientations: UIInterfaceOrientationMask

    init(device: UIDevice = .current) {
        let defaultOrientations: UIInterfaceOrientationMask = device.userInterfaceIdiom == .phone ? .portrait : .all

        self.supportedInterfaceOrientations = defaultOrientations
        self.defaultSupportedInterfaceOrientations = defaultOrientations

        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleSupportedInterfaceOrientationsDidChange(_:)),
            name: .supportedInterfaceOrientationsDidChange,
            object: nil
        )
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleSupportedInterfaceOrientationsResetToDefault),
            name: .supportedInterfaceOrientationsResetToDefault,
            object: nil
        )
    }

    @objc
    private func handleSupportedInterfaceOrientationsDidChange(_ notification: Foundation.Notification) {
        guard let newSupportedInterfaceOrientations = notification.object as? UIInterfaceOrientationMask else {
            return
        }

        supportedInterfaceOrientations = newSupportedInterfaceOrientations
    }

    @objc
    private func handleSupportedInterfaceOrientationsResetToDefault() {
        supportedInterfaceOrientations = defaultSupportedInterfaceOrientations
        UIViewController.attemptRotationToDeviceOrientation()
    }
}
