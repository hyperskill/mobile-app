import Foundation
import shared

final class AuthNewUserPlaceholderViewModel: FeatureViewModel<
  PlaceholderNewUserFeatureState,
  PlaceholderNewUserFeatureMessage,
  PlaceholderNewUserFeatureActionViewAction
> {
    func logViewedEvent() {
        onNewMessage(PlaceholderNewUserFeatureMessagePlaceholderViewedEventMessage())
    }
}
