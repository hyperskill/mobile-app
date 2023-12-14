import Foundation
import shared

final class OnboardingViewModel: FeatureViewModel<
  WelcomeFeatureState,
  WelcomeFeatureMessage,
  WelcomeFeatureActionViewAction
> {
    weak var moduleOutput: OnboardingOutputProtocol?

    var stateKs: WelcomeFeatureStateKs { .init(state) }

    init(feature: Presentation_reduxFeature) {
        super.init(feature: feature)
        onNewMessage(WelcomeFeatureMessageInitialize(forceUpdate: false))
    }

    override func shouldNotifyStateDidChange(
        oldState: WelcomeFeatureState,
        newState: WelcomeFeatureState
    ) -> Bool {
        WelcomeFeatureStateKs(oldState) != WelcomeFeatureStateKs(newState)
    }

    func doRetryLoadOnboarding() {
        onNewMessage(WelcomeFeatureMessageInitialize(forceUpdate: true))
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
    func doClickedSignUpAction() {
        onNewMessage(WelcomeFeatureMessageClickedSignUn())
    }

    func doSignUpPresentation(isInSignUpMode: Bool) {
        moduleOutput?.handleOnboardingSignUpRequested(isInSignUpMode: isInSignUpMode)
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(WelcomeFeatureMessageViewedEventMessage())
    }

    private func logClickedSignInEvent() {
        onNewMessage(WelcomeFeatureMessageClickedSignInEventMessage())
    }
}
