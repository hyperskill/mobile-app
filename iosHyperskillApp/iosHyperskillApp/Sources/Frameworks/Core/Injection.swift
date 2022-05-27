import Foundation
import shared

enum AppGraphBridge {
    static let shared: iOSAppComponent = AppGraphImpl(userAgentInfo: UserAgentBuilder.userAgentInfo)
}

// MARK: Cache

extension Settings {
    static var `default`: Multiplatform_settingsSettings {
        Settings.shared.makeAppleSettings(userDefaults: .standard)
    }
}

// MARK: Remote

extension AuthDataBuilder {
    static let sharedAuthorizationFlow = AuthDataBuilder.shared.provideAuthorizationFlow()

    static let sharedAuthorizationMutex = AuthDataBuilder.shared.provideAuthorizationMutex()
}
