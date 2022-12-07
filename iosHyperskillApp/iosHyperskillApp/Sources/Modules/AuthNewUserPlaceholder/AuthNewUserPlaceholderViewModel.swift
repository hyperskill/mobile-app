import Foundation
import shared

final class AuthNewUserPlaceholderViewModel: FeatureViewModel<
  PlaceholderNewUserFeatureState,
  PlaceholderNewUserFeatureMessage,
  PlaceholderNewUserFeatureActionViewAction
> {
    weak var moduleOutput: AuthNewUserPlaceholderOutputProtocol?

    var stateKs: PlaceholderNewUserFeatureStateKs { .init(state) }

    override func shouldNotifyStateDidChange(
        oldState: PlaceholderNewUserFeatureState,
        newState: PlaceholderNewUserFeatureState
    ) -> Bool {
        PlaceholderNewUserFeatureStateKs(oldState) != PlaceholderNewUserFeatureStateKs(newState)
    }

    func doSignIn() {
        //onNewMessage(PlaceholderNewUserFeatureMessagePlaceholderSignInTappedMessage())
    }

    func doAuthScreenPresentation() {
        moduleOutput?.handleAuthNewUserPlaceholderSignInRequested()
    }

    func doContinueOnWebPresentation() {
        //onNewMessage(PlaceholderNewUserFeatureMessageClickedContinueOnWeb())
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(PlaceholderNewUserFeatureMessageViewedEventMessage())
    }
}
