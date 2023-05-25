import CombineSchedulers
import Foundation
import shared

final class TrackSelectionDetailsViewModel: FeatureViewModel<
  TrackSelectionDetailsFeatureViewState,
  TrackSelectionDetailsFeatureMessage,
  TrackSelectionDetailsFeatureActionViewAction
> {
    var stateKs: TrackSelectionDetailsFeatureViewStateKs { .init(state) }

    override init(feature: Presentation_reduxFeature, mainScheduler: AnySchedulerOf<RunLoop> = .main) {
        super.init(feature: feature, mainScheduler: mainScheduler)
        onNewMessage(TrackSelectionDetailsFeatureMessageInitialize())
    }

    override func shouldNotifyStateDidChange(
        oldState: TrackSelectionDetailsFeatureViewState,
        newState: TrackSelectionDetailsFeatureViewState
    ) -> Bool {
        TrackSelectionDetailsFeatureViewStateKs(oldState) != TrackSelectionDetailsFeatureViewStateKs(newState)
    }

    func doRetryLoadTrackSelectionDetails() {
        onNewMessage(TrackSelectionDetailsFeatureMessageRetryContentLoading())
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(TrackSelectionDetailsFeatureMessageViewedEventMessage())
    }
}
