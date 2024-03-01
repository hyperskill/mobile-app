import Foundation

enum ApplicationShortcutIdentifier: String {
    case sendFeedback = "SendFeedback"

    init?(fullIdentifier: String) {
        guard let shortIdentifier = fullIdentifier.components(separatedBy: ".").last else {
            return nil
        }
        self.init(rawValue: shortIdentifier)
    }
}
