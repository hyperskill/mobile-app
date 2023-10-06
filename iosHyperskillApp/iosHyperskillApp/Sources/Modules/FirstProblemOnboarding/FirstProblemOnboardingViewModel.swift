import Foundation
import shared

final class FirstProblemOnboardingViewModel: FeatureViewModel<
FirstProblemOnboardingFeatureViewState,
FirstProblemOnboardingFeatureMessage,
FirstProblemOnboardingFeatureActionViewAction
> {
    weak var moduleOutput: FirstProblemOnboardingOutputProtocol?

    override func shouldNotifyStateDidChange(
        oldState: FirstProblemOnboardingFeatureViewState,
        newState: FirstProblemOnboardingFeatureViewState
    ) -> Bool {
        FirstProblemOnboardingFeatureViewStateKs(oldState) != FirstProblemOnboardingFeatureViewStateKs(newState)
    }

    func doPrimaryAction() {
        onNewMessage(FirstProblemOnboardingFeatureMessageLearningActionButtonClicked())
    }

    func doCompleteOnboarding(stepRoute: StepRoute?) {
        moduleOutput?.handleFirstProblemOnboardingCompleted(stepRoute: stepRoute)
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(FirstProblemOnboardingFeatureMessageViewedEventMessage())
    }
}
