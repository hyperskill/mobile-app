import shared
import SwiftUI

final class ProjectSelectionDetailsAssembly: UIKitAssembly {
    private let trackId: Int64
    private let projectId: Int64

    private let isProjectSelected: Bool
    private let isProjectBestRated: Bool
    private let isProjectFastestToComplete: Bool

    init(
        trackId: Int64,
        projectId: Int64,
        isProjectSelected: Bool,
        isProjectBestRated: Bool,
        isProjectFastestToComplete: Bool
    ) {
        self.trackId = trackId
        self.projectId = projectId
        self.isProjectSelected = isProjectSelected
        self.isProjectBestRated = isProjectBestRated
        self.isProjectFastestToComplete = isProjectFastestToComplete
    }

    func makeModule() -> UIViewController {
        let projectSelectionDetailsComponent = AppGraphBridge.sharedAppGraph.buildProjectSelectionDetailsComponent()

        let projectSelectionDetailsParams = ProjectSelectionDetailsParams(
            trackId: trackId,
            projectId: projectId,
            isProjectSelected: isProjectSelected,
            isProjectBestRated: isProjectBestRated,
            isProjectFastestToComplete: isProjectFastestToComplete
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
            rootView: projectSelectionDetailsView,
            appearance: .withoutBackButtonTitle
        )
        hostingController.navigationItem.largeTitleDisplayMode = .always
        hostingController.title = ""

        return hostingController
    }
}
