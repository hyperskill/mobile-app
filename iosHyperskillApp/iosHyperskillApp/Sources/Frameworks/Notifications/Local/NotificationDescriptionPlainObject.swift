import Foundation
import shared

struct NotificationDescriptionPlainObject {
    let title: String
    let text: String
}

extension NotificationDescriptionPlainObject {
    init(notificationDescription: NotificationDescription) {
        self.title = notificationDescription.title
        self.text = notificationDescription.text
    }
}
