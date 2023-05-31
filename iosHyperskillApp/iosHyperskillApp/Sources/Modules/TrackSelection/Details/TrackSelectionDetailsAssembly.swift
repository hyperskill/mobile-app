import shared
import SwiftUI

final class TrackSelectionDetailsAssembly: UIKitAssembly {
    private let isNewUserMode: Bool

    private let trackWithProgress: TrackWithProgress
    private let isTrackSelected: Bool
    private let isNewUserMode: Bool

    init(isNewUserMode: Bool, trackWithProgress: TrackWithProgress, isTrackSelected: Bool) {
        self.isNewUserMode = isNewUserMode
        self.trackWithProgress = trackWithProgress
        self.isTrackSelected = isTrackSelected
        self.isNewUserMode = isNewUserMode
    }

    func makeModule() -> UIViewController {
        let trackSelectionDetailsComponent = AppGraphBridge.sharedAppGraph.buildTrackSelectionDetailsComponent()

        let trackSelectionDetailsParams = TrackSelectionDetailsParams(
            trackWithProgress: trackWithProgress,
            isTrackSelected: isTrackSelected,
            isNewUserMode: isNewUserMode
        )
        let trackSelectionDetailsViewModel = TrackSelectionDetailsViewModel(
            feature: trackSelectionDetailsComponent.trackSelectionDetailsFeature(
                trackSelectionDetailsParams: trackSelectionDetailsParams
            )
        )

        let stackRouter = SwiftUIStackRouter()

        let trackSelectionDetailsView = TrackSelectionDetailsView(
            viewModel: trackSelectionDetailsViewModel,
            stackRouter: stackRouter
        )

        let hostingController = StyledHostingController(
            rootView: trackSelectionDetailsView,
            appearance: .withoutBackButtonTitle
        )
        hostingController.hidesBottomBarWhenPushed = true
        hostingController.navigationItem.largeTitleDisplayMode = .always
        hostingController.title = trackWithProgress.track.title

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
