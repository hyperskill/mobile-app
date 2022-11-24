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

    func doStudyPlanInWebPresentation() {
        onNewMessage(TrackFeatureMessageClickedContinueInWeb())
    }

    func makeViewData(track: Track, trackProgress: TrackProgress, studyPlan: StudyPlan?) -> TrackViewData {
        viewDataMapper.mapTrackDataToViewData(track: track, trackProgress: trackProgress, studyPlan: studyPlan)
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(TrackFeatureMessageViewedEventMessage())
    }
}
