import CombineSchedulers
import Foundation
import shared

final class FirstProblemOnboardingViewModel: FeatureViewModel<
FirstProblemOnboardingFeatureViewState,
FirstProblemOnboardingFeatureMessage,
FirstProblemOnboardingFeatureActionViewAction
> {
    weak var moduleOutput: FirstProblemOnboardingOutputProtocol?

    var stateKs: FirstProblemOnboardingFeatureViewStateKs { .init(state) }

    override func shouldNotifyStateDidChange(
        oldState: FirstProblemOnboardingFeatureViewState,
        newState: FirstProblemOnboardingFeatureViewState
    ) -> Bool {
        FirstProblemOnboardingFeatureViewStateKs(oldState) != FirstProblemOnboardingFeatureViewStateKs(newState)
    }

    override init(feature: Presentation_reduxFeature, mainScheduler: AnySchedulerOf<RunLoop> = .main) {
        super.init(feature: feature)

        onNewMessage(FirstProblemOnboardingFeatureMessageInitialize())
    }

    func doRetryContentLoading() {
        onNewMessage(FirstProblemOnboardingFeatureMessageRetryContentLoading())
    }

    func doCallToAction() {
        onNewMessage(FirstProblemOnboardingFeatureMessageCallToActionButtonClicked())
    }

    func doCompleteOnboarding(stepRoute: StepRoute?) {
        moduleOutput?.handleFirstProblemOnboardingCompleted(stepRoute: stepRoute)
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(FirstProblemOnboardingFeatureMessageViewedEventMessage())
    }
}
