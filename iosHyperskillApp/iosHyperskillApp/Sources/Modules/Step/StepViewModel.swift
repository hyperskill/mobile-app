import shared
import SwiftUI

final class StepViewModel: FeatureViewModel<StepFeatureState, StepFeatureMessage, StepFeatureActionViewAction> {
    private let stepID: Int

    private let viewDataMapper: StepViewDataMapper

    var stateKs: StepFeatureStateKs { .init(state) }

    init(stepID: Int, viewDataMapper: StepViewDataMapper, feature: Presentation_reduxFeature) {
        self.stepID = stepID
        self.viewDataMapper = viewDataMapper
        super.init(feature: feature)
    }

    override func shouldNotifyStateDidChange(oldState: StepFeatureState, newState: StepFeatureState) -> Bool {
        StepFeatureStateKs(oldState) != StepFeatureStateKs(newState)
    }

    func loadStep(forceUpdate: Bool = false) {
        onNewMessage(StepFeatureMessageInitialize(stepId: Int64(stepID), forceUpdate: forceUpdate))
    }

    func makeViewData(_ step: Step) -> StepViewData {
        viewDataMapper.mapStepToViewData(step)
    }

    // MARK: Analytic

    func logClickedBackEvent() {
        onNewMessage(StepFeatureMessageClickedBackEventMessage(stepId: Int64(stepID)))
    }
}
