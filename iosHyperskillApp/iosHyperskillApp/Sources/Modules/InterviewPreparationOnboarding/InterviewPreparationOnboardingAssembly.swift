import shared
import SwiftUI

final class InterviewPreparationOnboardingAssembly: UIKitAssembly {
    private let stepRoute: StepRoute

    init(stepRoute: StepRoute) {
        self.stepRoute = stepRoute
    }

    func makeModule() -> UIViewController {
        let interviewPreparationOnboardingComponent =
          AppGraphBridge.sharedAppGraph.buildInterviewPreparationOnboardingComponent()

        let interviewPreparationOnboardingViewModel = InterviewPreparationOnboardingViewModel(
            feature: interviewPreparationOnboardingComponent.interviewPreparationOnboardingFeature(
                stepRoute: stepRoute
            )
        )

        let interviewPreparationOnboardingView = InterviewPreparationOnboardingView(
            viewModel: interviewPreparationOnboardingViewModel
        )

        let hostingController = StyledHostingController(
            rootView: interviewPreparationOnboardingView
        )
        hostingController.navigationItem.largeTitleDisplayMode = .never

        return hostingController
    }
}
