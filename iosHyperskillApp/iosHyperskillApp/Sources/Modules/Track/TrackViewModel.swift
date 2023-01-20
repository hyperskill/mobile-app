import shared
import SwiftUI

final class TrackViewModel: FeatureViewModel<TrackFeatureState, TrackFeatureMessage, TrackFeatureActionViewAction> {
    private let viewDataMapper: TrackViewDataMapper

    var trackStateKs: TrackFeatureTrackStateKs { .init(state.trackState) }
    var gamificationToolbarStateKs: GamificationToolbarFeatureStateKs { .init(state.toolbarState) }

    init(viewDataMapper: TrackViewDataMapper, feature: Presentation_reduxFeature) {
        self.viewDataMapper = viewDataMapper
        super.init(feature: feature)
    }

    override func shouldNotifyStateDidChange(oldState: TrackFeatureState, newState: TrackFeatureState) -> Bool {
        !oldState.isEqual(newState)
    }

    func doLoadTrack(forceUpdate: Bool = false) {
        onNewMessage(TrackFeatureMessageInitialize(forceUpdate: forceUpdate))
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
            TrackFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessageClickedStreak(screen: GamificationToolbarScreen.track)
            )
        )
    }

    func doGemsBarButtonItemAction() {
        onNewMessage(
            TrackFeatureMessageGamificationToolbarMessage(
                message: GamificationToolbarFeatureMessageClickedGems(screen: GamificationToolbarScreen.track)
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
