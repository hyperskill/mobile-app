import shared
import SwiftUI

final class TrackViewModel: FeatureViewModel<TrackFeatureState, TrackFeatureMessage, TrackFeatureActionViewAction> {
    private let viewDataMapper: TrackViewDataMapper

    var trackStateKs: TrackFeatureTrackStateKs { .init(state.trackState) }
    var navigationBarItemsStateKs: NavigationBarItemsFeatureStateKs { .init(state.navigationBarItemsState) }

    init(viewDataMapper: TrackViewDataMapper, feature: Presentation_reduxFeature) {
        self.viewDataMapper = viewDataMapper
        super.init(feature: feature)
    }

    override func shouldNotifyStateDidChange(oldState: TrackFeatureState, newState: TrackFeatureState) -> Bool {
        !oldState.isEqual(newState)
    }

    func doLoadTrack(forceUpdate: Bool = false) {
        onNewMessage(TrackFeatureMessageInitialize(forceUpdate: forceUpdate))
        onNewMessage(
            TrackFeatureMessageNavigationBarItemsMessage(
                message: NavigationBarItemsFeatureMessageInitialize(
                    screen: NavigationBarItemsScreen.track,
                    forceUpdate: forceUpdate
                )
            )
        )
    }

    func doPullToRefresh() {
        onNewMessage(TrackFeatureMessagePullToRefresh())
    }

    func doTheoryTopicPresentation(topic: TrackViewData.TheoryTopic) {
        onNewMessage(TrackFeatureMessageTopicToDiscoverNextClicked(topicId: topic.id))
    }

    func doStudyPlanInWebPresentation() {
        onNewMessage(TrackFeatureMessageClickedContinueInWeb())
    }

    func doStreakBarButtonItemAction() {
        onNewMessage(
            TrackFeatureMessageNavigationBarItemsMessage(
                message: NavigationBarItemsFeatureMessageClickedStreak(screen: NavigationBarItemsScreen.track)
            )
        )
    }

    func doGemsBarButtonItemAction() {
        onNewMessage(
            TrackFeatureMessageNavigationBarItemsMessage(
                message: NavigationBarItemsFeatureMessageClickedGems(screen: NavigationBarItemsScreen.track)
            )
        )
    }

    func makeViewData(
        track: Track,
        trackProgress: TrackProgress,
        studyPlan: StudyPlan?,
        topicsToDiscoverNext: [Topic]
    ) -> TrackViewData {
        viewDataMapper.mapTrackDataToViewData(
            track: track,
            trackProgress: trackProgress,
            studyPlan: studyPlan,
            topicsToDiscoverNext: topicsToDiscoverNext
        )
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(TrackFeatureMessageViewedEventMessage())
    }
}
