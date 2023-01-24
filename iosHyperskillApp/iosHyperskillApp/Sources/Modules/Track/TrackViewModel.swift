import shared
import SwiftUI

final class TrackViewModel: FeatureViewModel<TrackFeatureState, TrackFeatureMessage, TrackFeatureActionViewAction> {
    private let viewDataMapper: TrackViewDataMapper

    var trackStateKs: TrackFeatureTrackStateKs { .init(state.trackState) }
    var gamificationToolbarStateKs: GamificationToolbarFeatureStateKs { .init(state.toolbarState) }
    var topicsToDiscoverNextStateKs: TopicsToDiscoverNextFeatureStateKs { .init(state.topicsToDiscoverNextState) }

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

    func doReloadTopicsToDiscoverNext() {
        onNewMessage(
            TrackFeatureMessageTopicsToDiscoverNextMessage(
                message: TopicsToDiscoverNextFeatureMessageInitialize(forceUpdate: true)
            )
        )
    }

    func doPullToRefresh() {
        onNewMessage(TrackFeatureMessagePullToRefresh())
    }

    func doTheoryTopicPresentation(topicID: Int64) {
        onNewMessage(TrackFeatureMessageTopicToDiscoverNextClicked(topicId: topicID))
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
        studyPlan: StudyPlan?
    ) -> TrackViewData {
        viewDataMapper.mapTrackDataToViewData(
            track: track,
            trackProgress: trackProgress,
            studyPlan: studyPlan
        )
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(TrackFeatureMessageViewedEventMessage())
    }
}
