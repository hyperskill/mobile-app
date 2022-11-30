import Foundation
import Sentry
import shared

final class SentryManager: shared.SentryManager {
    static let shared = SentryManager()

    // MARK: - Protocol Conforming -

    func setup() {
        SentrySDK.start { options in
            options.dsn = SentryInfo.dsn

            options.environment = "\(ApplicationInfo.flavor)-\(BuildType.current.rawValue)"

            let userAgentInfo = UserAgentBuilder.userAgentInfo
            options.releaseName = "\(userAgentInfo.versionName) (\(userAgentInfo.versionCode))"

            options.enableAutoPerformanceTracking = true
            options.enableUIViewControllerTracking = true
            options.enableOutOfMemoryTracking = false

            #if DEBUG
            options.debug = true
            options.diagnosticLevel = .warning

            options.tracesSampleRate = 1
            #else
            options.tracesSampleRate = 0.3
            #endif
        }
    }

    // MARK: Breadcrumbs

    func addBreadcrumb(breadcrumb: HyperskillSentryBreadcrumb) {
        let crumb = breadcrumb.sentryBreadcrumb
        SentrySDK.addBreadcrumb(crumb: crumb)
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

    // MARK: Identify Users

    func setUsedId(userId: String) {
        let user = Sentry.User()
        user.userId = userId

        SentrySDK.setUser(user)
    }

    func clearCurrentUser() {
        SentrySDK.setUser(nil)
    }
}
