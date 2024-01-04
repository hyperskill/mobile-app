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

        let stackRouter = StackRouter()

        let interviewPreparationOnboardingView = InterviewPreparationOnboardingView(
            viewModel: interviewPreparationOnboardingViewModel,
            stackRouter: stackRouter
        )

        let hostingController = StyledHostingController(
            rootView: interviewPreparationOnboardingView
        )
        hostingController.hidesBottomBarWhenPushed = true
        hostingController.navigationItem.largeTitleDisplayMode = .never
        hostingController.title = Strings.InterviewPreparationOnboarding.navigationTitle

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
