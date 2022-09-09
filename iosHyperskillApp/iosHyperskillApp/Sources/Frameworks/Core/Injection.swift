import Foundation
import shared

enum AppGraphBridge {
    static let sharedAppGraph: iOSAppComponent = AppGraphImpl(
        userAgentInfo: UserAgentBuilder.userAgentInfo,
        buildVariant: BuildType.current.buildVariant
    )
}
