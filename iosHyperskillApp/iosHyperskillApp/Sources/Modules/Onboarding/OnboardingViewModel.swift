import Foundation
import shared

final class OnboardingViewModel: FeatureViewModel<
  OnboardingFeatureState,
  OnboardingFeatureMessage,
  OnboardingFeatureActionViewAction
> {
    weak var moduleOutput: OnboardingOutputProtocol?

    var stateKs: OnboardingFeatureStateKs { .init(state) }

    override func shouldNotifyStateDidChange(
        oldState: OnboardingFeatureState,
        newState: OnboardingFeatureState
    ) -> Bool {
        OnboardingFeatureStateKs(oldState) != OnboardingFeatureStateKs(newState)
    }

    func loadOnboarding(forceUpdate: Bool = false) {
        onNewMessage(OnboardingFeatureMessageInitialize(forceUpdate: forceUpdate))
    }

    func doSignPresentation() {
        logClickedSignInEvent()
        moduleOutput?.handleOnboardingSignInRequested()
    }

    func doClickedSignUpAction() {
        onNewMessage(OnboardingFeatureMessageClickedSignUn())
    }

    func doSignUpPresentation(isInSignUpMode: Bool) {
        moduleOutput?.handleOnboardingSignUpRequested(isInSignUpMode: isInSignUpMode)
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(OnboardingFeatureMessageViewedEventMessage())
    }

    private func logClickedSignInEvent() {
        onNewMessage(OnboardingFeatureMessageClickedSignInEventMessage())
    }
}
