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

    func doSelectTrackButtonClicked() {
        onNewMessage(TrackSelectionDetailsFeatureMessageSelectTrackButtonClicked())
    }

    func doNavigateToHomeAsNewRootScreenPresentation() {
        NotificationCenter.default.post(
            name: .trackSelectionDetailsDidRequestNavigateToHomeAsNewRootScreen,
            object: nil
        )
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(TrackSelectionDetailsFeatureMessageViewedEventMessage())
    }
}

// MARK: - TrackSelectionDetailsViewModel (NSNotification.Name) -

extension NSNotification.Name {
    static let trackSelectionDetailsDidRequestNavigateToHomeAsNewRootScreen =
      NSNotification.Name("TrackSelectionDetailsDidRequestNavigateToHomeAsNewRootScreen")
}
