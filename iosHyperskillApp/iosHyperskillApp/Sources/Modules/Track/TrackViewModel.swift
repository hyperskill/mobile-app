import shared
import SwiftUI

final class TrackViewModel: FeatureViewModel<TrackFeatureState, TrackFeatureMessage, TrackFeatureActionViewAction> {
    private let viewDataMapper: TrackViewDataMapper

    init(viewDataMapper: TrackViewDataMapper, feature: Presentation_reduxFeature) {
        self.viewDataMapper = viewDataMapper
        super.init(feature: feature)
    }

    func loadTrack(forceUpdate: Bool = false) {
        onNewMessage(TrackFeatureMessageInit(forceUpdate: forceUpdate))
    }

    func logViewedEvent() {
        onNewMessage(TrackFeatureMessageTrackViewedEventMessage())
    }

    func makeViewData(track: Track, trackProgress: TrackProgress, studyPlan: StudyPlan?) -> TrackViewData {
        viewDataMapper.mapTrackDataToViewData(track: track, trackProgress: trackProgress, studyPlan: studyPlan)
    }

    func doStudyPlanInWebPresentation() {
        guard state is TrackFeatureStateContent,
              let url = HyperskillURLFactory.makeStudyPlan() else {
            return
        }

        WebControllerManager.shared.presentWebControllerWithURL(
            url,
            withKey: .externalLink,
            allowsSafari: true,
            backButtonStyle: .done
        )
    }
}
