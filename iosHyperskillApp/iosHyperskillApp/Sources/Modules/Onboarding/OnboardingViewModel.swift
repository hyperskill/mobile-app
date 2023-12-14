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
        onNewMessage(WelcomeFeatureMessageClickedSignUn())
    }

    func doSecondaryButtonAction() {
        onNewMessage(WelcomeFeatureMessageClickedSignInEventMessage())
        moduleOutput?.handleOnboardingSignInRequested()
    }

    func doSignInPresentation() {
        moduleOutput?.handleOnboardingSignInRequested()
    }

    func doSignUpPresentation(isInSignUpMode: Bool) {
        moduleOutput?.handleOnboardingSignUpRequested(isInSignUpMode: isInSignUpMode)
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(WelcomeFeatureMessageViewedEventMessage())
    }
}
