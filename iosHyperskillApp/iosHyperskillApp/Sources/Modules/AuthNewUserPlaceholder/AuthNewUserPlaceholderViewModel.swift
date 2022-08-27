import Foundation
import shared

final class AuthNewUserPlaceholderViewModel: FeatureViewModel<
  PlaceholderNewUserFeatureState,
  PlaceholderNewUserFeatureMessage,
  PlaceholderNewUserFeatureActionViewAction
> {
    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(PlaceholderNewUserFeatureMessagePlaceholderViewedEventMessage())
    }

    func logClickedContinueEvent() {
        onNewMessage(PlaceholderNewUserFeatureMessagePlaceholderClickedContinueEventMessage())
    }
}
