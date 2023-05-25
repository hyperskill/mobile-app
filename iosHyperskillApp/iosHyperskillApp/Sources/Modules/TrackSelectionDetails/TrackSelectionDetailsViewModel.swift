import Foundation
import shared

final class TrackSelectionDetailsViewModel: FeatureViewModel<
  TrackSelectionDetailsFeatureViewState,
  TrackSelectionDetailsFeatureMessage,
  TrackSelectionDetailsFeatureActionViewAction
> {
    var stateKs: TrackSelectionDetailsFeatureViewStateKs { .init(state) }

    override func shouldNotifyStateDidChange(
        oldState: TrackSelectionDetailsFeatureViewState,
        newState: TrackSelectionDetailsFeatureViewState
    ) -> Bool {
        // TrackSelectionDetailsFeatureViewStateKs(oldState) != TrackSelectionDetailsFeatureViewStateKs(newState)
        true
    }

    func doLoadTrackSelectionDetails() {
        onNewMessage(TrackSelectionDetailsFeatureMessageInitialize())
    }

    func doRetryLoadTrackSelectionDetails() {
        onNewMessage(TrackSelectionDetailsFeatureMessageRetryContentLoading())
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(TrackSelectionDetailsFeatureMessageViewedEventMessage())
    }
}
