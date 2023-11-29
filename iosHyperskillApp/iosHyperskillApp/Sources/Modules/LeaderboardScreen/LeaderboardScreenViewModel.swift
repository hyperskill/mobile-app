import Foundation
import shared

final class LeaderboardScreenViewModel: FeatureViewModel<
  LeaderboardScreenFeature.ViewState,
  LeaderboardScreenFeatureMessage,
  LeaderboardScreenFeatureActionViewAction
> {
    var listViewStateKs: LeaderboardScreenFeatureListViewStateKs { .init(state.listViewState) }

    init(feature: Presentation_reduxFeature) {
        super.init(feature: feature)
        onNewMessage(LeaderboardScreenFeatureMessageInitialize())
    }

    override func shouldNotifyStateDidChange(
        oldState: LeaderboardScreenFeature.ViewState,
        newState: LeaderboardScreenFeature.ViewState
    ) -> Bool {
        !oldState.isEqual(newState)
    }

    func doRetryLoadLeaderboardScreen() {
        onNewMessage(LeaderboardScreenFeatureMessageRetryContentLoading())
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(LeaderboardScreenFeatureMessageViewedEventMessage())
    }
}
