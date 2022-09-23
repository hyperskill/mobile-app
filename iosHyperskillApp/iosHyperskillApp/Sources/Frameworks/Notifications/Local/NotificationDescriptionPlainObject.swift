import Foundation
import shared

struct NotificationDescriptionPlainObject {
    let id: Int
    let title: String
    let text: String
}

extension NotificationDescriptionPlainObject {
    init(notificationDescription: NotificationDescription) {
        self.id = Int(notificationDescription.id)
        self.title = notificationDescription.title
        self.text = notificationDescription.text
    }
}
