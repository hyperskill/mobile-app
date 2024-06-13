import shared
import SwiftUI

final class UsersQuestionnaireOnboardingAssembly: UIKitAssembly {
    private weak var moduleOutput: UsersQuestionnaireOnboardingOutputProtocol?

    init(moduleOutput: UsersQuestionnaireOnboardingOutputProtocol?) {
        self.moduleOutput = moduleOutput
    }

    func makeModule() -> UIViewController {
        let usersQuestionnaireOnboardingComponent =
            AppGraphBridge.sharedAppGraph.buildLegacyUsersQuestionnaireOnboardingComponent()

        let usersQuestionnaireOnboardingViewModel = UsersQuestionnaireOnboardingViewModel(
            feature: usersQuestionnaireOnboardingComponent.usersQuestionnaireOnboardingFeature
        )
        usersQuestionnaireOnboardingViewModel.moduleOutput = moduleOutput

        let usersQuestionnaireOnboardingView = UsersQuestionnaireOnboardingView(
            viewModel: usersQuestionnaireOnboardingViewModel
        )

        let hostingController = StyledHostingController(
            rootView: usersQuestionnaireOnboardingView
        )
        hostingController.navigationItem.largeTitleDisplayMode = .never

        return hostingController
    }
}
