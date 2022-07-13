import shared
import SwiftUI

final class TrackViewModel: FeatureViewModel<TrackFeatureState, TrackFeatureMessage, TrackFeatureActionViewAction> {
    private let trackID: Int

    private let viewDataMapper: TrackViewDataMapper

    init(trackID: Int, viewDataMapper: TrackViewDataMapper, feature: Presentation_reduxFeature) {
        self.trackID = trackID
        self.viewDataMapper = viewDataMapper
        super.init(feature: feature)
    }

    func loadTrack(forceUpdate: Bool = false) {
        onNewMessage(TrackFeatureMessageInit(trackId: Int64(trackID), forceUpdate: forceUpdate))
    }

    func makeViewData(track: Track, trackProgress: TrackProgress, studyPlan: StudyPlan?) -> TrackViewData {
        viewDataMapper.mapTrackDataToViewData(track: track, trackProgress: trackProgress, studyPlan: studyPlan)
    }

    func presentStudyPlanInWeb() {
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
