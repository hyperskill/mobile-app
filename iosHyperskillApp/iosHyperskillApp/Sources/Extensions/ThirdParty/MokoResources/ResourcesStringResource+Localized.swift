import Foundation
import shared

extension ResourcesStringResource {
    func localized() -> String {
        return NSLocalizedString(self.resourceId, bundle: self.bundle, comment: "")
    }
}
