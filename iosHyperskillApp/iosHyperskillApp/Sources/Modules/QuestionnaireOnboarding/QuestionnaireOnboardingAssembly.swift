import shared
import SwiftUI

final class QuestionnaireOnboardingAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let questionnaireOnboardingComponent = AppGraphBridge.sharedAppGraph.buildQuestionnaireOnboardingComponent()

        let questionnaireOnboardingViewModel = QuestionnaireOnboardingViewModel(
            feature: questionnaireOnboardingComponent.questionnaireOnboardingFeature
        )

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
