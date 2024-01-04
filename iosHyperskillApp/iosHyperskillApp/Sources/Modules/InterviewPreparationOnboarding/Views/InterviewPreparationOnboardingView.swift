import shared
import SwiftUI

struct InterviewPreparationOnboardingView: View {
    @StateObject var viewModel: InterviewPreparationOnboardingViewModel

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            InterviewPreparationOnboardingContentView()
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
        case .navigateTo:
            break
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
