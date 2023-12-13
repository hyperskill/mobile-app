import Foundation
import shared

final class OnboardingViewModel: FeatureViewModel<
  OnboardingFeatureState,
  OnboardingFeatureMessage,
  OnboardingFeatureActionViewAction
> {
    weak var moduleOutput: OnboardingOutputProtocol?

    var stateKs: OnboardingFeatureStateKs { .init(state) }

    init(feature: Presentation_reduxFeature) {
        super.init(feature: feature)
        onNewMessage(OnboardingFeatureMessageInitialize(forceUpdate: false))
    }

    override func shouldNotifyStateDidChange(
        oldState: OnboardingFeatureState,
        newState: OnboardingFeatureState
    ) -> Bool {
        OnboardingFeatureStateKs(oldState) != OnboardingFeatureStateKs(newState)
    }

    func doRetryLoadOnboarding() {
        onNewMessage(OnboardingFeatureMessageInitialize(forceUpdate: true))
    }

    func doPrimaryButtonAction() {
        onNewMessage(OnboardingFeatureMessageClickedSignUn())
    }

    func doSecondaryButtonAction() {
        logClickedSignInEvent()
        moduleOutput?.handleOnboardingSignInRequested()
    }

    func doSignPresentation() {
        moduleOutput?.handleOnboardingSignInRequested()
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
