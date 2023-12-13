import Foundation
import shared

final class LeaderboardViewModel: FeatureViewModel<
  LeaderboardScreenFeature.ViewState,
  LeaderboardScreenFeatureMessage,
  LeaderboardScreenFeatureActionViewAction
> {
    private var isScreenBecomesActiveFirstTime = true

    var listViewStateKs: LeaderboardScreenFeatureListViewStateKs { .init(state.listViewState) }
    var gamificationToolbarStateKs: GamificationToolbarFeatureStateKs { .init(state.toolbarState) }

    override func shouldNotifyStateDidChange(
        oldState: LeaderboardScreenFeature.ViewState,
        newState: LeaderboardScreenFeature.ViewState
    ) -> Bool {
        !oldState.isEqual(newState)
    }

    func doLoadLeaderboard() {
        onNewMessage(LeaderboardScreenFeatureMessageInitialize())
    }

    func doRetryLoadLeaderboard() {
        onNewMessage(LeaderboardScreenFeatureMessageRetryContentLoading())
    }

    func doScreenBecomesActive() {
        if isScreenBecomesActiveFirstTime {
            isScreenBecomesActiveFirstTime = false
        } else {
            onNewMessage(LeaderboardScreenFeatureMessageScreenBecomesActive())
        }
    }

    func doSelectTab(_ tab: LeaderboardScreenFeature.Tab) {
        onNewMessage(LeaderboardScreenFeatureMessageTabClicked(tab: tab))
    }

    func doListItemTapAction(item: LeaderboardListItem) {
        onNewMessage(LeaderboardScreenFeatureMessageListItemClicked(userId: Int64(item.userId)))
    }

    // MARK: GamificationToolbar

    func doStreakBarButtonItemAction() {
        onNewMessage(
            LeaderboardScreenFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessageClickedStreak()
            )
        )
    }

    func doGemsBarButtonItemAction() {
        onNewMessage(
            LeaderboardScreenFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessageClickedGems()
            )
        )
    }

    func doProgressBarButtonItemAction() {
        onNewMessage(
            LeaderboardScreenFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessageClickedProgress()
            )
        )
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(LeaderboardScreenFeatureMessageViewedEventMessage())
    }
}