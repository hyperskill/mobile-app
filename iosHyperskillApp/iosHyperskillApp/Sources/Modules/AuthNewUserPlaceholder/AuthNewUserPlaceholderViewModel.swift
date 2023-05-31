// swiftlint:disable:next file_header
//import Foundation
//import shared
//
//final class AuthNewUserPlaceholderViewModel: FeatureViewModel<
//  PlaceholderNewUserFeatureState,
//  PlaceholderNewUserFeatureMessage,
//  PlaceholderNewUserFeatureActionViewAction
//> {
//    weak var moduleOutput: AuthNewUserPlaceholderOutputProtocol?
//
//    var stateKs: PlaceholderNewUserFeatureStateKs { .init(state) }
//
//    override func shouldNotifyStateDidChange(
//        oldState: PlaceholderNewUserFeatureState,
//        newState: PlaceholderNewUserFeatureState
//    ) -> Bool {
//        PlaceholderNewUserFeatureStateKs(oldState) != PlaceholderNewUserFeatureStateKs(newState)
//    }
//
//    func doLoadContent(forceUpdate: Bool = false) {
//        onNewMessage(PlaceholderNewUserFeatureMessageInitialize(forceUpdate: forceUpdate))
//    }
//
//    func doTrackModalPresentation(trackID: Int64) {
//        onNewMessage(PlaceholderNewUserFeatureMessageTrackClicked(trackId: trackID))
//    }
//
//    func doTrackStartLearningAction(trackID: Int64) {
//        onNewMessage(PlaceholderNewUserFeatureMessageStartLearningButtonClicked(trackId: trackID))
//    }
//
//    func doHomeScreenPresentation() {
//        moduleOutput?.handleAuthNewUserPlaceholderDidRequestNavigateToHome()
//    }
//
//    // MARK: Analytic
//
//    func logViewedEvent() {
//        onNewMessage(PlaceholderNewUserFeatureMessageViewedEventMessage())
//    }
//
//    func logTrackModalShownEvent(trackID: Int64) {
//        onNewMessage(PlaceholderNewUserFeatureMessageTrackModalShownEventMessage(trackId: trackID))
//    }
//
//    func logTrackModalHiddenEvent(trackID: Int64) {
//        onNewMessage(PlaceholderNewUserFeatureMessageTrackModalHiddenEventMessage(trackId: trackID))
//    }
//}
