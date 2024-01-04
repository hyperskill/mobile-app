import shared
import SwiftUI

struct InterviewPreparationOnboardingView: View {
    @StateObject var viewModel: InterviewPreparationOnboardingViewModel

    let stackRouter: StackRouterProtocol

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            InterviewPreparationOnboardingContentView(
                onCallToActionButtonTap: viewModel.doCallToAction
            )
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear {
            viewModel.stopListening()
            viewModel.onViewAction = nil
        }
    }
}

// MARK: - InterviewPreparationOnboardingView (ViewAction) -

private extension InterviewPreparationOnboardingView {
    func handleViewAction(
        _ viewAction: InterviewPreparationOnboardingFeatureActionViewAction
    ) {
        switch InterviewPreparationOnboardingFeatureActionViewActionKs(viewAction) {
        case .navigateTo(let navigateToViewAction):
            handleNavigateToViewAction(navigateToViewAction)
        }
    }

    func handleNavigateToViewAction(_ viewAction: InterviewPreparationOnboardingFeatureActionViewActionNavigateTo) {
        switch InterviewPreparationOnboardingFeatureActionViewActionNavigateToKs(viewAction) {
        case .stepScreen(let navigateToStepScreenViewAction):
            let assembly = StepAssembly(stepRoute: navigateToStepScreenViewAction.stepRoute)
            stackRouter.pushViewController(assembly.makeModule())
        }
    }
}

// MARK: - InterviewPreparationOnboardingView (Preview) -

@available(iOS 17, *)
#Preview {
    InterviewPreparationOnboardingAssembly(
        stepRoute: StepRouteInterviewPreparation(stepId: 0)
    ).makeModule()
}
