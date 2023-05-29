import shared
import SwiftUI

final class TrackSelectionDetailsAssembly: UIKitAssembly {
    private let trackWithProgress: TrackWithProgress
    private let isTrackSelected: Bool
    private let isNewUserMode: Bool

    init(trackWithProgress: TrackWithProgress, isTrackSelected: Bool, isNewUserMode: Bool) {
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

        let trackSelectionDetailsView = TrackSelectionDetailsView(
            viewModel: trackSelectionDetailsViewModel
        )

        let hostingController = StyledHostingController(
            rootView: trackSelectionDetailsView,
            appearance: .withoutBackButtonTitle
        )
        hostingController.hidesBottomBarWhenPushed = true
        hostingController.navigationItem.largeTitleDisplayMode = .always
        hostingController.title = trackWithProgress.track.title

        return hostingController
    }
}
