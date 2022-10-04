import Foundation
import Sentry
import shared

enum SentryManager {
    static func configure() {
        SentrySDK.start { options in
            options.dsn = SentryInfo.dsn

            options.environment = "\(ApplicationInfo.flavor)-\(BuildType.current.rawValue)"

            let userAgentInfo = UserAgentBuilder.userAgentInfo
            options.releaseName = "\(userAgentInfo.versionName) (\(userAgentInfo.versionCode))"

            options.enableAutoPerformanceTracking = true
            options.enableUIViewControllerTracking = true

            #if DEBUG
            options.debug = true
            options.diagnosticLevel = .warning

            options.tracesSampleRate = 1
            #else
            options.tracesSampleRate = 0.3
            #endif
        }
    }

    static func updateUserID(_ userID: String) {
        let user = Sentry.User()
        user.userId = userID

        SentrySDK.setUser(user)
    }

    static func clearCurrentUser() {
        SentrySDK.setUser(nil)
    }

    // MARK: Capture

    static func capture(error: KotlinThrowable) {
        SentrySDK.capture(error: error.asError())
    }

    static func captureErrorMessage(error: KotlinThrowable) {
        captureErrorMessage(String(describing: error.asError()))
    }

    static func captureErrorMessage(_ message: String) {
        SentrySDK.capture(message: message) { scope in
            scope.setLevel(.error)
        }
    }
}
