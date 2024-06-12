import shared
import UIKit

final class AppAssembly: UIKitAssembly {
    private let pushNotificationData: PushNotificationData?

    init(pushNotificationData: PushNotificationData?) {
        self.pushNotificationData = pushNotificationData
    }

    func makeModule() -> UIViewController {
        let feature = AppGraphBridge.sharedAppGraph.legacyMainComponent.legacyAppFeature()

        let viewModel = AppViewModel(
            pushNotificationData: pushNotificationData,
            analytic: AppGraphBridge.sharedAppGraph.analyticComponent.analyticInteractor,
            feature: feature
        )

        let router = AppRouter()

        let viewController = AppViewController(viewModel: viewModel, router: router)
        viewModel.viewController = viewController
        router.viewController = viewController

        return viewController
    }
}
