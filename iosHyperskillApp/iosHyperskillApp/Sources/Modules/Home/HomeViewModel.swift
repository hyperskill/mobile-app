import Foundation
import shared

final class HomeViewModel: FeatureViewModel<HomeFeatureState, HomeFeatureMessage, HomeFeatureActionViewAction> {
    func loadContent(forceUpdate: Bool = false) {
        onNewMessage(HomeFeatureMessageInit(forceUpdate: forceUpdate))
    }

    func logViewedEvent() {
        onNewMessage(HomeFeatureMessageHomeViewedEventMessage())
    }
}

// MARK: - HomeViewModel: ProblemOfDayOutputProtocol -

extension HomeViewModel: ProblemOfDayOutputProtocol {
    func handleProblemOfDayReloadRequested() {
        loadContent(forceUpdate: true)
    }
}
