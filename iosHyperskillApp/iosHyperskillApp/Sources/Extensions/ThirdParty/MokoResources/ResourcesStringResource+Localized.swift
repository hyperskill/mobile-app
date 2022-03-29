import Foundation
import shared

extension ResourcesStringResource {
    func localized() -> String {
        // // swiftlint:disable:next nslocalizedstring_key
        NSLocalizedString(self.resourceId, bundle: self.bundle, comment: "")
    }
}
