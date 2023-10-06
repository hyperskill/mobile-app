import shared
import SwiftUI

final class FirstProblemOnboardingAssembly: UIKitAssembly {
    private let isNewUserMode: Bool

    private weak var moduleOutput: FirstProblemOnboardingOutputProtocol?

    init(
        isNewUserMode: Bool,
        output: FirstProblemOnboardingOutputProtocol?
    ) {
        self.isNewUserMode = isNewUserMode
        self.moduleOutput = output
    }

    func makeModule() -> UIViewController {
        let firstProblemOnboardingComponent = AppGraphBridge.sharedAppGraph.buildFirstProblemOnboardingComponent()

        let viewModel = FirstProblemOnboardingViewModel(
            feature: firstProblemOnboardingComponent.firstProblemOnboardingFeature(isNewUserMode: isNewUserMode)
        )
        viewModel.moduleOutput = moduleOutput

        let notificationsOnboardingView = FirstProblemOnboardingView(
            viewModel: viewModel
        )

        let hostingController = StyledHostingController(
            rootView: notificationsOnboardingView
        )
        hostingController.navigationItem.largeTitleDisplayMode = .never

        return hostingController
    }
}
