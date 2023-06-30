import Foundation
import shared

final class ProgressScreenViewModel: FeatureViewModel<
  ProgressScreenViewState,
  ProgressScreenFeatureMessage,
  ProgressScreenFeatureActionViewAction
> {
    var trackProgressViewStateKs: ProgressScreenViewStateTrackProgressViewStateKs {
        ProgressScreenViewStateTrackProgressViewStateKs(state.trackProgressViewState)
    }

    var projectProgressViewStateKs: ProgressScreenViewStateProjectProgressViewStateKs {
        ProgressScreenViewStateProjectProgressViewStateKs(state.projectProgressViewState)
    }

    init(feature: Presentation_reduxFeature) {
        super.init(feature: feature)
        onNewMessage(ProgressScreenFeatureMessageInitialize())
    }

    override func shouldNotifyStateDidChange(
        oldState: ProgressScreenViewState,
        newState: ProgressScreenViewState
    ) -> Bool {
        !oldState.isEqual(newState)
    }

    func doPullToRefresh() {
        onNewMessage(ProgressScreenFeatureMessagePullToRefresh())
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(ProgressScreenFeatureMessageViewedEventMessage())
    }
}
