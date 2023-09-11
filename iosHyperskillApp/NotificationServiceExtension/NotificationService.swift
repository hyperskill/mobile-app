import UserNotifications

final class NotificationService: UNNotificationServiceExtension {
    private var contentHandler: ((UNNotificationContent) -> Void)?
    private var bestAttemptContent: UNMutableNotificationContent?

    override func didReceive(
        _ request: UNNotificationRequest,
        withContentHandler contentHandler: @escaping (UNNotificationContent) -> Void
    ) {
        self.contentHandler = contentHandler
        bestAttemptContent = (request.content.mutableCopy() as? UNMutableNotificationContent)

        guard let bestAttemptContent else {
            return
        }
        
        // Modify the notification content here...
        bestAttemptContent.title = "\(bestAttemptContent.title) [modified]"

        guard let fcmOptions = request.content.userInfo["fcm_options"] as? [AnyHashable : Any],
              let imageString = fcmOptions["image"] as? String,
              let imageURL = URL(string: imageString) else {
            return contentHandler(bestAttemptContent)
        }

        Task {
            defer { contentHandler(bestAttemptContent) }

            do {
                let (data, response) = try await URLSession.shared.data(from: imageURL)

                let file = response.suggestedFilename ?? imageURL.lastPathComponent
                let destination = URL(fileURLWithPath: NSTemporaryDirectory()).appendingPathComponent(file)
                try data.write(to: destination)

                let attachment = try UNNotificationAttachment(identifier: "", url: destination)
                bestAttemptContent.attachments = [attachment]

                contentHandler(bestAttemptContent)
            } catch {
                #if DEBUG
                print("NotificationService :: image attachment error = \(error)")
                #endif
            }
        }
    }
    
    override func serviceExtensionTimeWillExpire() {
        if let contentHandler, let bestAttemptContent {
            contentHandler(bestAttemptContent)
        }
    }
}
