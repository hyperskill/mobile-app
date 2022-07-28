import Foundation
import shared

final class ProblemOfDayViewModel {
    weak var moduleOutput: ProblemOfDayOutputProtocol?

    private let problemOfDayState: HomeFeatureProblemOfDayState
    private let viewDataMapper: ProblemOfDayViewDataMapper

    init(problemOfDayState: HomeFeatureProblemOfDayState, viewDataMapper: ProblemOfDayViewDataMapper) {
        self.problemOfDayState = problemOfDayState
        self.viewDataMapper = viewDataMapper
    }

    func doReloadAction() {
        moduleOutput?.handleProblemOfDayReloadRequested()
    }

    func makeViewData() -> ProblemOfDayViewData {
        viewDataMapper.mapProblemOfDayStateToViewData(problemOfDayState)
    }
}
