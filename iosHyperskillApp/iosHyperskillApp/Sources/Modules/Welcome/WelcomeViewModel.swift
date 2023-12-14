import Foundation
import shared

final class WelcomeViewModel: FeatureViewModel<
  WelcomeFeatureState,
  WelcomeFeatureMessage,
  WelcomeFeatureActionViewAction
> {
    weak var moduleOutput: WelcomeOutputProtocol?

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

    func doRetryContentLoading() {
        onNewMessage(WelcomeFeatureMessageInitialize(forceUpdate: true))
    }

    func doPrimaryButtonAction() {
        onNewMessage(WelcomeFeatureMessageClickedSignUn())
    }

    func doSecondaryButtonAction() {
        onNewMessage(WelcomeFeatureMessageClickedSignInEventMessage())
        moduleOutput?.handleWelcomeSignInRequested()
    }

    func doSignInPresentation() {
        moduleOutput?.handleWelcomeSignInRequested()
    }

    func doSignUpPresentation(isInSignUpMode: Bool) {
        moduleOutput?.handleWelcomeSignUpRequested(isInSignUpMode: isInSignUpMode)
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(WelcomeFeatureMessageViewedEventMessage())
    }
}
