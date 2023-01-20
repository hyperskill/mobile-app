import shared
import SwiftUI

final class TrackViewModel: FeatureViewModel<TrackFeatureState, TrackFeatureMessage, TrackFeatureActionViewAction> {
    private let viewDataMapper: TrackViewDataMapper

    var stateKs: TrackFeatureStateKs { .init(state) }

    init(viewDataMapper: TrackViewDataMapper, feature: Presentation_reduxFeature) {
        self.viewDataMapper = viewDataMapper
        super.init(feature: feature)
    }

    override func shouldNotifyStateDidChange(oldState: TrackFeatureState, newState: TrackFeatureState) -> Bool {
        TrackFeatureStateKs(oldState) != TrackFeatureStateKs(newState)
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

extension TrackViewModel: StepOutputProtocol {
    func handleStepReloading(stepRoute: StepRoute) {
        self.onViewAction?(TrackFeatureActionViewActionReloadStep(stepRoute: stepRoute))
    }
}
