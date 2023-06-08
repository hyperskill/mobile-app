import CombineSchedulers
import Foundation
import shared

final class TrackSelectionListViewModel: FeatureViewModel<
  TrackSelectionListFeatureViewState,
  TrackSelectionListFeatureMessage,
  TrackSelectionListFeatureActionViewAction
> {
    var viewStateKs: TrackSelectionListFeatureViewStateKs { .init(state) }

    override init(feature: Presentation_reduxFeature, mainScheduler: AnySchedulerOf<RunLoop> = .main) {
        super.init(feature: feature, mainScheduler: mainScheduler)
        onNewMessage(TrackSelectionListFeatureMessageInitialize())
    }

    override func shouldNotifyStateDidChange(
        oldState: TrackSelectionListFeatureViewState,
        newState: TrackSelectionListFeatureViewState
    ) -> Bool {
        TrackSelectionListFeatureViewStateKs(oldState) != TrackSelectionListFeatureViewStateKs(newState)
    }

    func doRetryLoadTrackSelectionList() {
        onNewMessage(TrackSelectionListFeatureMessageRetryContentLoading())
    }

    func doMainTrackAction(trackID: Int64) {
        onNewMessage(TrackSelectionListFeatureMessageTrackClicked(trackId: trackID))
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(TrackSelectionListFeatureMessageViewedEventMessage())
    }
}
