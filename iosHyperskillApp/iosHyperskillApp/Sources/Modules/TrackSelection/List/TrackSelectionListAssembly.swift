import SwiftUI
import shared

final class TrackSelectionListAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let trackSelectionListComponent = AppGraphBridge.sharedAppGraph.buildTrackSelectionListComponent()

        let trackSelectionListViewModel = TrackSelectionListViewModel(
            feature: trackSelectionListComponent.trackSelectionListFeature(params: TrackSelectionListParams(isNewUserMode: false))
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
