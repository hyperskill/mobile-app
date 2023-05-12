import SwiftUI

final class ProjectSelectionListAssembly: Assembly {
    private let trackID: Int64

    init(trackID: Int64) {
        self.trackID = trackID
    }

    func makeModule() -> ProjectSelectionRootView {
        let projectSelectionListComponent = AppGraphBridge.sharedAppGraph.buildProjectSelectionListComponent()

        let viewModel = ProjectSelectionListViewModel(
            feature: projectSelectionListComponent.projectSelectionListFeature(trackId: trackID)
        )

        return ProjectSelectionRootView(viewModel: viewModel)
    }
}
