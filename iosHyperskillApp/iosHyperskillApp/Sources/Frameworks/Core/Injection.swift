import Foundation
import shared

enum AppGraphBridge {
    static let sharedAppGraph: iOSAppComponent = iOSAppComponentImpl(
        userAgentInfo: UserAgentBuilder.userAgentInfo,
        buildVariant: BuildType.current.buildVariant,
        sentryManager: SentryManager.shared
    )
}
