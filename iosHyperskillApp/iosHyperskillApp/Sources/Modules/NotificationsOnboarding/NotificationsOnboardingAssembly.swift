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

        let panModalPresenter = PanModalPresenter()

        let notificationsOnboardingView = NotificationsOnboardingView(
            viewModel: notificationsOnboardingViewModel,
            panModalPresenter: panModalPresenter
        )

        let hostingController = StyledHostingController(
            rootView: notificationsOnboardingView
        )
        hostingController.navigationItem.largeTitleDisplayMode = .never

        panModalPresenter.rootViewController = hostingController

        return hostingController
    }
}
