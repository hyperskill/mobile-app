import Foundation
import shared

final class HomeViewModel: FeatureViewModel<HomeFeatureState, HomeFeatureMessage, HomeFeatureActionViewAction> {
    func loadContent(forceUpdate: Bool = false) {
        self.onNewMessage(HomeFeatureMessageInit(forceUpdate: forceUpdate))
    }
}

// MARK: - HomeViewModel: ProblemOfDayOutputProtocol -

extension HomeViewModel: ProblemOfDayOutputProtocol {
    func handleProblemOfDayReloadRequested() {
        loadContent(forceUpdate: true)
    }
}
