import shared
import SwiftUI

final class TrackSelectionListAssembly: UIKitAssembly {
    private let isNewUserMode: Bool

    init(isNewUserMode: Bool) {
        self.isNewUserMode = isNewUserMode
    }

    func makeModule() -> UIViewController {
        let trackSelectionListComponent = AppGraphBridge.sharedAppGraph.buildTrackSelectionListComponent()

        let trackSelectionListParams = TrackSelectionListParams(
            isNewUserMode: isNewUserMode
        )
        let trackSelectionListViewModel = TrackSelectionListViewModel(
            feature: trackSelectionListComponent.trackSelectionListFeature(
                params: trackSelectionListParams
            )
        )

        let stackRouter = SwiftUIStackRouter()
        let trackSelectionListView = TrackSelectionListView(
            viewModel: trackSelectionListViewModel,
            stackRouter: stackRouter
        )

        let hostingController = StyledHostingController(
            rootView: trackSelectionListView,
            appearance: .withoutBackButtonTitle
        )
        hostingController.hidesBottomBarWhenPushed = true
        hostingController.navigationItem.largeTitleDisplayMode = .never
        hostingController.title = Strings.TrackSelectionList.title

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
