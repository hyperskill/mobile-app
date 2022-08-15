import Foundation
import shared

struct ReminderLocalNotification: LocalNotificationProtocol {
    var title: String

    var body: String

    var identifier: String { "ReminderLocalNotification" }

    var trigger: UNNotificationTrigger?

    private var selectedHour: Int

    init(
        notificationDescritpion: NotificationDescription,
        selectedHour: Int
    ) {
        self.title = notificationDescritpion.title
        self.body = notificationDescritpion.text
        self.selectedHour = selectedHour
        if let dateComponents = self.dateComponents {
            self.trigger = UNCalendarNotificationTrigger(dateMatching: dateComponents, repeats: true)
        }
    }

    private var dateComponents: DateComponents? {
        guard let date = Calendar.current.date(
            byAdding: .day,
            value: 1,
            to: Date()
        ) else {
            return nil
        }

        var reminderDateComponents = Calendar.current.dateComponents([.hour, .day, .month, .year], from: date)

        reminderDateComponents.hour = self.selectedHour

        return reminderDateComponents
    }
}
