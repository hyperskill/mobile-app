import SwiftUI

final class TrackSelectionListAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let trackSelectionListComponent = AppGraphBridge.sharedAppGraph.buildTrackSelectionListComponent()

        let trackSelectionListViewModel = TrackSelectionListViewModel(
            feature: trackSelectionListComponent.trackSelectionListFeature
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
        hostingController.navigationItem.largeTitleDisplayMode = .never

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
