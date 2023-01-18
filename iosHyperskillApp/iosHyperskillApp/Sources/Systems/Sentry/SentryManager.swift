import Foundation
import Sentry
import shared

final class SentryManager: shared.SentryManager {
    static let shared = SentryManager()

    private var currentTransactionsDict = [UInt: PlatformHyperskillSentryTransaction]()

    // MARK: - Protocol Conforming -

    func setup() {
        SentrySDK.start { options in
            options.dsn = SentryInfo.dsn

            options.environment = "\(ApplicationInfo.flavor)-\(BuildType.current.rawValue)"

            let userAgentInfo = UserAgentBuilder.userAgentInfo
            options.releaseName = "\(userAgentInfo.versionName) (\(userAgentInfo.versionCode))"

            #if DEBUG
            options.debug = true
            options.diagnosticLevel = .info

            options.tracesSampleRate = 1
            #else
            options.tracesSampleRate = 0.3
            #endif

            // HTTP Client Errors
            options.enableCaptureFailedRequests = true

            options.failedRequestStatusCodes = [
                // Client errors
                HttpStatusCodeRange(min: 400, max: 407),
                HttpStatusCodeRange(min: 409, max: 499),
                // Server errors
                HttpStatusCodeRange(min: 500, max: 599)
            ]
            options.failedRequestTargets = [ApplicationInfo.host]

            // Swizzling
            options.enableSwizzling = true
            // Watchdog Terminations
            options.enableWatchdogTerminationTracking = false
            // App Hangs
            options.enableAppHangTracking = true
            // Performance Monitoring
            options.enableAutoPerformanceTracing = true
            options.enableUIViewControllerTracing = true
            options.enableNetworkTracking = true
            options.enableFileIOTracing = false
            options.enableCoreDataTracing = false
        }
    }

    // MARK: Breadcrumbs

    func addBreadcrumb(breadcrumb: HyperskillSentryBreadcrumb) {
        let crumb = breadcrumb.sentryBreadcrumb
        SentrySDK.addBreadcrumb(crumb)
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

    // MARK: Transactions

    func containsOngoingTransaction(transaction: HyperskillSentryTransaction) -> Bool {
        currentTransactionsDict[mapTransactionToKey(transaction)] != nil
    }

    func startTransaction(transaction: HyperskillSentryTransaction) {
        let span = SentrySDK.startTransaction(name: transaction.name, operation: transaction.operation)
        let platformTransaction = PlatformHyperskillSentryTransaction(
            span: span,
            name: transaction.name,
            operation_: transaction.operation
        )
        currentTransactionsDict[mapTransactionToKey(platformTransaction)] = platformTransaction
    }

    func finishTransaction(transaction: HyperskillSentryTransaction, throwable: KotlinThrowable?) {
        guard let platformTransaction = currentTransactionsDict[mapTransactionToKey(transaction)] else {
            return
        }

        if let throwable {
            platformTransaction.span.setData(
                value: String(describing: throwable),
                key: HyperskillSentryTransactionKeyValues.shared.DATA_ERROR
            )
            platformTransaction.span.finish(status: .unknownError)
        } else {
            platformTransaction.span.finish(status: .ok)
        }

        currentTransactionsDict.removeValue(forKey: mapTransactionToKey(platformTransaction))
    }

    private func mapTransactionToKey(_ transaction: HyperskillSentryTransaction) -> UInt {
        transaction.hash()
    }
}
