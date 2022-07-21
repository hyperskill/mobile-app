import shared
import SwiftUI

final class AppViewModel: FeatureViewModel<AppFeatureState, AppFeatureMessage, AppFeatureActionViewAction> {
    func loadApp(forceUpdate: Bool = false) {
        onNewMessage(AppFeatureMessageInit(forceUpdate: forceUpdate))
    }
}
