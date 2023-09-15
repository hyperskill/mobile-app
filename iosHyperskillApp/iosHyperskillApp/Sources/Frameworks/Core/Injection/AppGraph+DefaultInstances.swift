import Foundation
import shared

extension AnalyticInteractor {
    static var `default`: AnalyticInteractor {
        AppGraphBridge.sharedAppGraph.analyticComponent.analyticInteractor
    }
}

extension NotificationInteractor {
    static var `default`: NotificationInteractor {
        AppGraphBridge.sharedAppGraph.buildNotificationComponent().notificationInteractor
    }
}

extension PushNotificationsInteractor {
    static var `default`: PushNotificationsInteractor {
        AppGraphBridge.sharedAppGraph.buildPushNotificationsComponent().pushNotificationsInteractor
    }
}
