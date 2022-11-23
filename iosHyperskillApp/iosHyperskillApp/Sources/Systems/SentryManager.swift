import Foundation
import Sentry
import shared

final class SentryManager: shared.SentryManager {
    static let shared = SentryManager()

    func setup() {
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

    func addBreadcrumb(breadcrumb: HyperskillSentryBreadcrumb) {
        let crumb = breadcrumb.sentryBreadcrumb
        print(crumb)
        SentrySDK.addBreadcrumb(crumb: crumb)
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

    func captureMessage(message: String, level: HyperskillSentryLevel) {
        SentrySDK.capture(message: message) { scope in
            scope.setLevel(level.sentryLevel)
        }
    }

    func captureErrorMessage(message: String) {
        captureMessage(message: message, level: HyperskillSentryLevel.error)
    }

    #warning("DELETE")
    static func capture(error: KotlinThrowable) {
        SentrySDK.capture(error: error.asError())
    }

    #warning("DELETE")
    static func captureErrorMessage(error: KotlinThrowable) {
        captureErrorMessage(String(describing: error.asError()))
    }

    #warning("DELETE")
    static func captureErrorMessage(_ message: String) {
        SentrySDK.capture(message: message) { scope in
            scope.setLevel(.error)
        }
    }
}
