import Foundation
import shared

enum AppGraphBridge {
    static let shared: iOSAppComponent = AppGraphImpl(userAgentInfo: UserAgentBuilder.userAgentInfo)
}
