import shared
import SwiftUI

final class StepViewModel: FeatureViewModel<StepFeatureState, StepFeatureMessage, StepFeatureActionViewAction> {
    private let stepID: Int

    private let viewDataMapper: StepViewDataMapper

    init(stepID: Int, viewDataMapper: StepViewDataMapper, feature: Presentation_reduxFeature) {
        self.stepID = stepID
        self.viewDataMapper = viewDataMapper
        super.init(feature: feature)
    }

    func loadStep(forceUpdate: Bool = false) {
        self.onNewMessage(StepFeatureMessageInit(stepId: Int64(stepID), forceUpdate: forceUpdate))
    }

    func makeViewData(_ step: Step) -> StepViewData {
        self.viewDataMapper.mapStepToViewData(step)
    }

    // MARK: Analytic

    func logClickedBackEvent() {
        onNewMessage(StepFeatureMessageClickedBackEventMessage(stepId: Int64(stepID)))
    }
}
