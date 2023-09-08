import Foundation
import shared

class IOSAppComponentImpl: shared.SharedIOSAppComponentImpl {
    override func getIosFCMTokenProvider() -> IosFCMTokenProvider {
        IosFCMTokenProviderImpl()
    }
}

enum AppGraphBridge {
    static let sharedAppGraph: iOSAppComponent = IOSAppComponentImpl(
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
