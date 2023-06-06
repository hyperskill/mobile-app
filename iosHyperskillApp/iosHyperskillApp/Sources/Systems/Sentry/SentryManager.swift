import Foundation
import Sentry
import shared

final class SentryManager: shared.SentryManager {
    static let shared = SentryManager()

    private var currentTransactionsDict = [UInt: PlatformHyperskillSentryTransaction]()

    private var isAppInitializationTransactionFinished = false

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
        // Start app initialization transaction
        DispatchQueue.main.async {
            let transaction = HyperskillSentryTransactionBuilder.shared.buildAppInitialization(
                isAuthorized: UserDefaults.standard.isCurrentUserAuthorized
            )
            self.startTransaction(transaction: transaction)
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
        let span = handleStartTransactionAsAppInitializationChild(transaction)
          ?? SentrySDK.startTransaction(name: transaction.name, operation: transaction.operation)
        transaction.tags.forEach { (key, value) in
            span.setTag(value: value, key: key)
        }

        let platformTransaction = PlatformHyperskillSentryTransaction(
            span: span,
            name: transaction.name,
            operation: transaction.operation,
            tags: transaction.tags
        )
        currentTransactionsDict[mapTransactionToKey(platformTransaction)] = platformTransaction
    }

    func finishTransaction(transaction: HyperskillSentryTransaction, throwable: KotlinThrowable?) {
        defer {
            handleFinishTransactionAsAppInitializationChild(transaction)
        }

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

// MARK: - SentryManager (AppInitializationTransaction) -

private extension SentryManager {
    func getAppInitializationTransaction() -> PlatformHyperskillSentryTransaction? {
        let transaction = HyperskillSentryTransactionBuilder.shared.buildAppInitialization(
            isAuthorized: UserDefaults.standard.isCurrentUserAuthorized
        )
        let key = mapTransactionToKey(transaction)
        return currentTransactionsDict[key]
    }

    func handleStartTransactionAsAppInitializationChild(_ transaction: HyperskillSentryTransaction) -> Span? {
        guard transaction.isAppScreenRemoteDataLoading,
              let appInitializationTransaction = getAppInitializationTransaction() else {
            return nil
        }

        return appInitializationTransaction.span.startChild(
            operation: transaction.operation,
            description: transaction.name
        )
    }

    func handleFinishTransactionAsAppInitializationChild(_ transaction: HyperskillSentryTransaction) {
        guard transaction.isAppScreenRemoteDataLoading && !isAppInitializationTransactionFinished else {
            return
        }

        isAppInitializationTransactionFinished = true

        finishTransaction(
            transaction: HyperskillSentryTransactionBuilder.shared.buildAppInitialization(
                isAuthorized: UserDefaults.standard.isCurrentUserAuthorized
            ),
            throwable: nil
        )
    }
}

private extension HyperskillSentryTransaction {
    var isAppScreenRemoteDataLoading: Bool { name == "app-feature-remote-data-loading" }
}

private extension UserDefaults {
    var isCurrentUserAuthorized: Bool {
        UserDefaults.standard.string(forKey: AuthCacheKeyValues.shared.AUTH_RESPONSE) != nil
    }
}
