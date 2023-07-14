import shared
import UIKit

final class AppAssembly: UIKitAssembly {
    private let pushNotificationData: PushNotificationData?

    init(pushNotificationData: PushNotificationData?) {
        self.pushNotificationData = pushNotificationData
    }

    func makeModule() -> UIViewController {
        let feature = AppGraphBridge.sharedAppGraph.mainComponent.appFeature()

        let viewModel = AppViewModel(
            pushNotificationData: pushNotificationData,
            analytic: AppGraphBridge.sharedAppGraph.analyticComponent.analyticInteractor,
            feature: feature
        )

        let viewController = AppViewController(viewModel: viewModel)
        viewModel.viewController = viewController

        return viewController
    }
}
