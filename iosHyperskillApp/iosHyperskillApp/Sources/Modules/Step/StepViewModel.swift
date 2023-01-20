import shared
import SwiftUI

final class StepViewModel: FeatureViewModel<StepFeatureState, StepFeatureMessage, StepFeatureActionViewAction> {
    let stepRoute: StepRoute

    private let moduleOutput: StepOutputProtocol?

    private let viewDataMapper: StepViewDataMapper

    var stateKs: StepFeatureStateKs { .init(state) }

    init(
        stepRoute: StepRoute,
        moduleOutput: StepOutputProtocol?,
        viewDataMapper: StepViewDataMapper,
        feature: Presentation_reduxFeature
    ) {
        self.stepRoute = stepRoute
        self.moduleOutput = moduleOutput
        self.viewDataMapper = viewDataMapper
        super.init(feature: feature)
    }

    override func shouldNotifyStateDidChange(oldState: StepFeatureState, newState: StepFeatureState) -> Bool {
        StepFeatureStateKs(oldState) != StepFeatureStateKs(newState)
    }

    func loadStep(forceUpdate: Bool = false) {
        onNewMessage(StepFeatureMessageInitialize(forceUpdate: forceUpdate))
    }

    func makeViewData(_ step: Step) -> StepViewData {
        viewDataMapper.mapStepToViewData(step)
    }

    func doStartPracticing() {
        onNewMessage(StepFeatureMessageStartPracticingClicked())
    }

    func doStepReload(stepRoute: StepRoute) {
        moduleOutput?.handleStepReloading(stepRoute: stepRoute)
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(StepFeatureMessageViewedEventMessage())
    }
}
