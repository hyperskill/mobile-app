import shared
import SwiftUI

final class ProjectSelectionDetailsAssembly: UIKitAssembly {
    private let isNewUserMode: Bool

    private let trackId: Int64
    private let projectId: Int64

    private let isProjectSelected: Bool
    private let isProjectBestRated: Bool
    private let isProjectFastestToComplete: Bool

    init(
        isNewUserMode: Bool,
        trackId: Int64,
        projectId: Int64,
        isProjectSelected: Bool,
        isProjectBestRated: Bool,
        isProjectFastestToComplete: Bool
    ) {
        self.isNewUserMode = isNewUserMode
        self.trackId = trackId
        self.projectId = projectId
        self.isProjectSelected = isProjectSelected
        self.isProjectBestRated = isProjectBestRated
        self.isProjectFastestToComplete = isProjectFastestToComplete
    }

    func makeModule() -> UIViewController {
        let projectSelectionDetailsComponent = AppGraphBridge.sharedAppGraph.buildProjectSelectionDetailsComponent()

        let projectSelectionDetailsParams = ProjectSelectionDetailsParams(
            isNewUserMode: isNewUserMode,
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

        let stackRouter = SwiftUIStackRouter()

        let projectSelectionDetailsView = ProjectSelectionDetailsView(
            viewModel: projectSelectionDetailsViewModel,
            stackRouter: stackRouter
        )

        let hostingController = StyledHostingController(
            rootView: projectSelectionDetailsView,
            appearance: .withoutBackButtonTitle
        )
        hostingController.navigationItem.largeTitleDisplayMode = .always
        hostingController.title = ""

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
