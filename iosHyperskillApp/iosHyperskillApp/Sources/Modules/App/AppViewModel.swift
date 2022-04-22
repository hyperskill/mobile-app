import shared
import SwiftUI

final class AppViewModel: FeatureViewModel<AppFeatureState, AppFeatureMessage, AppFeatureActionViewAction> {
    override init(feature: Presentation_reduxFeature) {
        super.init(feature: feature)

        self.onNewMessage(AppFeatureMessageAppStarted())
    }
}
