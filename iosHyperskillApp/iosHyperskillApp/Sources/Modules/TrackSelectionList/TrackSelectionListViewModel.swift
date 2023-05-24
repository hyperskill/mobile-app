import Foundation
import shared

final class TrackSelectionListViewModel: FeatureViewModel<
  TrackSelectionListFeatureViewState,
  TrackSelectionListFeatureMessage,
  TrackSelectionListFeatureActionViewAction
> {
    var viewStateKs: TrackSelectionListFeatureViewStateKs { .init(state) }

    override func shouldNotifyStateDidChange(
        oldState: TrackSelectionListFeatureViewState,
        newState: TrackSelectionListFeatureViewState
    ) -> Bool {
        TrackSelectionListFeatureViewStateKs(oldState) != TrackSelectionListFeatureViewStateKs(newState)
    }

    func doLoadTrackSelectionList() {
        onNewMessage(TrackSelectionListFeatureMessageInitialize())
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
