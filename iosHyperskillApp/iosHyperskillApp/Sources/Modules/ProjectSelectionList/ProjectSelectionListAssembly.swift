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

        let rootView = ProjectSelectionListView(viewModel: viewModel)
        let hostingController = StyledHostingController(
            rootView: rootView,
            appearance: .withoutBackButtonTitle
        )

        return hostingController
    }
}
