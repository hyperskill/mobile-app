import Foundation
import shared

final class HomeViewModel: FeatureViewModel<HomeFeatureState, HomeFeatureMessage, HomeFeatureActionViewAction> {
    func loadContent(forceUpdate: Bool = false) {
        onNewMessage(HomeFeatureMessageInit(forceUpdate: forceUpdate))
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(HomeFeatureMessageHomeViewedEventMessage())
    }

    private func logClickedProblemOfDayCardEvent() {
        onNewMessage(HomeFeatureMessageHomeClickedProblemOfDayCardEventMessage())
    }
}

// MARK: - HomeViewModel: ProblemOfDayOutputProtocol -

extension HomeViewModel: ProblemOfDayOutputProtocol {
    func handleProblemOfDayReloadRequested() {
        loadContent(forceUpdate: true)
    }

    func handleProblemOfDayContinueActionPerformed() {
        logClickedProblemOfDayCardEvent()
    }
}
