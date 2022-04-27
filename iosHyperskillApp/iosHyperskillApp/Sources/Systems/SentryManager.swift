import Foundation
import Sentry

enum SentryManager {
    static func configure() {
        SentrySDK.start { options in
            options.dsn = SentryInfo.dsn

            let buildType: String = {
                #if DEBUG
                return "debug"
                #else
                return "release"
                #endif
            }()
            options.environment = "\(ApplicationInfo.flavor)-\(buildType)"

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
}
