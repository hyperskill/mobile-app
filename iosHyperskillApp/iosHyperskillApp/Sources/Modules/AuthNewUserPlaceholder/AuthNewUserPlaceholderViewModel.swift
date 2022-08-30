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
        moduleOutput?.handleAuthNewUserPlaceholderSignInRequested()
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(PlaceholderNewUserFeatureMessagePlaceholderViewedEventMessage())
    }

    func logClickedContinueEvent() {
        onNewMessage(PlaceholderNewUserFeatureMessagePlaceholderClickedContinueEventMessage())
    }
}
