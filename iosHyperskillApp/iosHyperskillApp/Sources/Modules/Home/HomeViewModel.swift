import Foundation
import shared

final class HomeViewModel: FeatureViewModel<
  HomeFeatureState,
  HomeFeatureMessage,
  HomeFeatureActionViewAction
> {
    func loadContent(forceUpdate: Bool = false) {
        self.onNewMessage(HomeFeatureMessageInit(forceUpdate: forceUpdate))
    }
}
