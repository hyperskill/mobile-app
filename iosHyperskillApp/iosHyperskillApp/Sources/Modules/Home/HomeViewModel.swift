import Foundation
import shared

final class HomeViewModel: FeatureViewModel<
  HomeFeatureState,
  HomeFeatureMessage,
  HomeFeatureActionViewAction
> {
    private let problemOfDayViewDataMapper: ProblemOfDayViewDataMapper

    init(problemOfDayViewDataMapper: ProblemOfDayViewDataMapper, feature: Presentation_reduxFeature) {
        self.problemOfDayViewDataMapper = problemOfDayViewDataMapper
        super.init(feature: feature)
    }

    func loadContent(forceUpdate: Bool = false) {
        self.onNewMessage(HomeFeatureMessageInit(forceUpdate: forceUpdate))
    }

    func makeProblemOfDayViewData() -> ProblemOfDayViewData? {
        if let data = self.state as? HomeFeatureStateContent {
            return problemOfDayViewDataMapper.mapProblemOfDayStateToViewData(data.problemOfDayState)
        }
        return nil
    }
}
