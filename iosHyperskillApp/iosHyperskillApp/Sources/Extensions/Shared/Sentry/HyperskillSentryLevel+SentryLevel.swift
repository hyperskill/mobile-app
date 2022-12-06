import Foundation
import Sentry
import shared

extension HyperskillSentryLevel {
    var sentryLevel: SentryLevel {
        switch self {
        case HyperskillSentryLevel.debug:
            return .debug
        case HyperskillSentryLevel.info:
            return .info
        case HyperskillSentryLevel.warning:
            return .warning
        case HyperskillSentryLevel.error:
            return .error
        case HyperskillSentryLevel.fatal:
            return .fatal
        default:
            assertionFailure("Did receive unsupported HyperskillSentryLevel = \(self)")
            return .none
        }
    }
}
