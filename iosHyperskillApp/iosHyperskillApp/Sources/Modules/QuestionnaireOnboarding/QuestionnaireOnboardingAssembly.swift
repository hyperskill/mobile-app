import shared
import SwiftUI

final class QuestionnaireOnboardingAssembly: UIKitAssembly {
    private weak var moduleOutput: QuestionnaireOnboardingOutputProtocol?

    init(moduleOutput: QuestionnaireOnboardingOutputProtocol?) {
        self.moduleOutput = moduleOutput
    }

    func makeModule() -> UIViewController {
        let questionnaireOnboardingComponent = AppGraphBridge.sharedAppGraph.buildUsersQuestionnaireOnboardingComponent()

        let questionnaireOnboardingViewModel = QuestionnaireOnboardingViewModel(
            feature: questionnaireOnboardingComponent.usersQuestionnaireOnboardingFeature
        )
        questionnaireOnboardingViewModel.moduleOutput = moduleOutput

        let questionnaireOnboardingView = QuestionnaireOnboardingView(
            viewModel: questionnaireOnboardingViewModel
        )

        let hostingController = StyledHostingController(
            rootView: questionnaireOnboardingView
        )
        hostingController.navigationItem.largeTitleDisplayMode = .never

        return hostingController
    }
}
