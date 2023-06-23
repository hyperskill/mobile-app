import Foundation
import shared

enum AppGraphBridge {
    static let sharedAppGraph: iOSAppComponent = iOSAppComponentImpl(
        userAgentInfo: UserAgentBuilder.userAgentInfo,
        buildVariant: BuildVariant.current,
        sentryManager: SentryManager.shared
    )
}
