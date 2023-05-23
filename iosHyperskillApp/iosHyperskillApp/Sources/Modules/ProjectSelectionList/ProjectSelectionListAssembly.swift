import SwiftUI

final class ProjectSelectionListAssembly: UIKitAssembly {
    private let trackID: Int64

    init(trackID: Int64) {
        self.trackID = trackID
    }

    func makeModule() -> UIViewController {
        let projectSelectionListComponent = AppGraphBridge.sharedAppGraph.buildProjectSelectionListComponent()

        let viewModel = ProjectSelectionListViewModel(
            feature: projectSelectionListComponent.projectSelectionListFeature(trackId: trackID)
        )

        let stackRouter = SwiftUIStackRouter()

        let rootView = ProjectSelectionListView(viewModel: viewModel, stackRouter: stackRouter)
        let hostingController = StyledHostingController(
            rootView: rootView,
            appearance: .withoutBackButtonTitle
        )
        // Fixes an issue with that SwiftUI view content layout unexpectedly pop/jumps on appear
        hostingController.navigationItem.largeTitleDisplayMode = .never

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
