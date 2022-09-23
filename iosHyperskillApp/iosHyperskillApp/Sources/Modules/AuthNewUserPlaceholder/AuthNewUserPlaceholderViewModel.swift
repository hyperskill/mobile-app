import Foundation
import shared

final class AuthNewUserPlaceholderViewModel: FeatureViewModel<
  PlaceholderNewUserFeatureState,
  PlaceholderNewUserFeatureMessage,
  PlaceholderNewUserFeatureActionViewAction
> {
    weak var moduleOutput: AuthNewUserPlaceholderOutputProtocol?

    func doSignIn() {
        onNewMessage(PlaceholderNewUserFeatureMessagePlaceholderSignInTappedMessage())
    }

    func doAuthScreenPresentation() {
        moduleOutput?.handleAuthNewUserPlaceholderSignInRequested()
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(PlaceholderNewUserFeatureMessageViewedEventMessage())
    }

    func logClickedContinueEvent() {
        onNewMessage(PlaceholderNewUserFeatureMessageClickedContinueEventMessage())
    }
}
