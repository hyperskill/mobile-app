import Foundation
import shared

enum AppGraphBridge {
    static let sharedAppGraph: iOSAppComponent = iOSAppComponentImpl(
        userAgentInfo: UserAgentBuilder.userAgentInfo,
        buildVariant: BuildVariant.current,
        sentryManager: SentryManager.shared
    )
}

// MARK: - Default Instances -

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
