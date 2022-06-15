import shared
import SwiftUI

final class StepQuizViewModel: FeatureViewModel<
  StepQuizFeatureState,
  StepQuizFeatureMessage,
  StepQuizFeatureActionViewAction
> {
    private let step: Step

    private let viewDataMapper: StepQuizViewDataMapper

    init(step: Step, viewDataMapper: StepQuizViewDataMapper, feature: Presentation_reduxFeature) {
        self.step = step
        self.viewDataMapper = viewDataMapper
        super.init(feature: feature)
    }

    func loadAttempt() {
        self.onNewMessage(StepQuizFeatureMessageInitWithStep(step: self.step, forceUpdate: false))
    }

    func makeViewData() -> StepQuizViewData {
        self.viewDataMapper.mapStepToViewData(self.step)
    }
}
