import shared
import SwiftUI

final class TrackViewModel: FeatureViewModel<TrackFeatureState, TrackFeatureMessage, TrackFeatureActionViewAction> {
    private let viewDataMapper: TrackViewDataMapper

    init(viewDataMapper: TrackViewDataMapper, feature: Presentation_reduxFeature) {
        self.viewDataMapper = viewDataMapper
        super.init(feature: feature)
    }

    func doLoadTrack(forceUpdate: Bool = false) {
        onNewMessage(TrackFeatureMessageInit(forceUpdate: forceUpdate))
    }

    func doPullToRefresh() {
        onNewMessage(TrackFeatureMessagePullToRefresh())
    }

    func doStudyPlanInWebPresentation() {
        logClickedContinueInWebEvent()

        WebControllerManager.shared.presentWebControllerWithNextURLPath(
            HyperskillUrlPath.StudyPlan(),
            controllerType: .safari
        )
    }

    func makeViewData(track: Track, trackProgress: TrackProgress, studyPlan: StudyPlan?) -> TrackViewData {
        viewDataMapper.mapTrackDataToViewData(track: track, trackProgress: trackProgress, studyPlan: studyPlan)
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(TrackFeatureMessageViewedEventMessage())
    }

    private func logClickedContinueInWebEvent() {
        onNewMessage(TrackFeatureMessageClickedContinueInWebEventMessage())
    }
}
