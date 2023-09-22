import shared
import SwiftUI

final class NotificationsOnboardingAssembly: UIKitAssembly {
    private weak var moduleOutput: NotificationsOnboardingOutputProtocol?

    init(output: NotificationsOnboardingOutputProtocol?) {
        self.moduleOutput = output
    }

    func makeModule() -> UIViewController {
        let notificationsOnboardingComponent = AppGraphBridge.sharedAppGraph.buildNotificationsOnboardingComponent()

        let notificationsOnboardingViewModel = NotificationsOnboardingViewModel(
            notificationsRegistrationService: .shared,
            feature: notificationsOnboardingComponent.notificationsOnboardingFeature
        )
        notificationsOnboardingViewModel.moduleOutput = moduleOutput

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
