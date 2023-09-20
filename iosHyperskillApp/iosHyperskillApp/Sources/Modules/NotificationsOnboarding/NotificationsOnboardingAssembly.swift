import shared
import SwiftUI

final class NotificationsOnboardingAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let notificationsOnboardingComponent = AppGraphBridge.sharedAppGraph.buildNotificationsOnboardingComponent()

        let notificationsOnboardingViewModel = NotificationsOnboardingViewModel(
            feature: notificationsOnboardingComponent.notificationOnboardingFeature
        )

        let notificationsOnboardingView = NotificationsOnboardingView(
            viewModel: notificationsOnboardingViewModel
        )

        let hostingController = StyledHostingController(
            rootView: notificationsOnboardingView
        )
        hostingController.navigationItem.largeTitleDisplayMode = .never

        return hostingController
    }
}
