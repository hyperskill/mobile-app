//import Foundation
//import shared
//
//final class ProgressScreenViewModel: FeatureViewModel<
//  ProgressScreenFeatureViewState,
//  ProgressScreenFeatureMessage,
//  ProgressScreenFeatureActionViewAction
//> {
//    var stateKs: ProgressScreenFeatureViewStateKs { .init(state) }
//
//    init(feature: Presentation_reduxFeature) {
//        super.init(feature: feature)
//        onNewMessage(ProgressScreenFeatureMessageInitialize())
//    }
//
//    override func shouldNotifyStateDidChange(
//        oldState: ProgressScreenFeatureViewState,
//        newState: ProgressScreenFeatureViewState
//    ) -> Bool {
//        // ProgressScreenFeatureViewStateKs(oldState) != ProgressScreenFeatureViewStateKs(newState)
//        true
//    }
//
//    func doRetryLoadProgressScreen() {
//        onNewMessage(ProgressScreenFeatureMessageRetryContentLoading())
//    }
//
//    // MARK: Analytic
//
//    func logViewedEvent() {
//        onNewMessage(ProgressScreenFeatureMessageViewedEventMessage())
//    }
//}
