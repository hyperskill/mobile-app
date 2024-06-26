import Foundation
import shared

private final class AppGraph: IosAppComponentImpl {
    override func getIosFCMTokenProvider() -> IosFCMTokenProvider {
        IosFCMTokenProviderImpl.shared
    }

    override func getIosPurchaseManager() -> shared.IosPurchaseManager {
        PurchaseManager.shared
    }
}

enum AppGraphBridge {
    static let sharedAppGraph: IosAppComponent = AppGraph(
        userAgentInfo: UserAgentBuilder.userAgentInfo,
        buildVariant: BuildVariant.current,
        sentryManager: SentryManager.shared,
        analyticEngines: [AppsFlyerManager.shared, AmplitudeManager.shared]
    )
}
