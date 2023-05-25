import shared
import SwiftUI

final class ProjectSelectionDetailsAssembly: UIKitAssembly {
    private let trackId: Int64
    private let projectId: Int64
    private let isProjectSelected: Bool

    init(trackId: Int64, projectId: Int64, isProjectSelected: Bool) {
        self.trackId = trackId
        self.projectId = projectId
        self.isProjectSelected = isProjectSelected
    }

    func makeModule() -> UIViewController {
        let projectSelectionDetailsComponent = AppGraphBridge.sharedAppGraph.buildProjectSelectionDetailsComponent()

        let projectSelectionDetailsParams = ProjectSelectionDetailsParams(
            trackId: trackId,
            projectId: projectId,
            isProjectSelected: isProjectSelected
        )
        let projectSelectionDetailsViewModel = ProjectSelectionDetailsViewModel(
            feature: projectSelectionDetailsComponent.projectSelectionDetailsFeature(
                projectSelectionDetailsParams: projectSelectionDetailsParams
            )
        )

        let projectSelectionDetailsView = ProjectSelectionDetailsView(
            viewModel: projectSelectionDetailsViewModel
        )

        let hostingController = StyledHostingController(
            rootView: projectSelectionDetailsView
        )
        hostingController.navigationItem.largeTitleDisplayMode = .always

        return hostingController
    }
}
