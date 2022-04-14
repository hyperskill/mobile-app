import shared
import SwiftUI

final class StepViewModel: FeatureViewModel<StepFeatureState, StepFeatureMessage, StepFeatureActionViewAction> {
    private let stepID: Int

    init(stepID: Int, feature: Presentation_reduxFeature) {
        self.stepID = stepID
        super.init(feature: feature)
    }

    func loadStep() {
        self.onNewMessage(StepFeatureMessageInit(stepId: Int64(self.stepID), forceUpdate: false))
    }
}
