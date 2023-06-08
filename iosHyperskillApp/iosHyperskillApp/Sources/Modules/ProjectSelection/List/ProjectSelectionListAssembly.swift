import shared
import SwiftUI

final class ProjectSelectionListAssembly: UIKitAssembly {
    private let isNewUserMode: Bool
    private let trackID: Int64

    init(isNewUserMode: Bool, trackID: Int64) {
        self.isNewUserMode = isNewUserMode
        self.trackID = trackID
    }

    func makeModule() -> UIViewController {
        let projectSelectionListComponent = AppGraphBridge.sharedAppGraph.buildProjectSelectionListComponent()

        let projectSelectionListParams = ProjectSelectionListParams(
            isNewUserMode: isNewUserMode,
            trackId: trackID
        )
        let projectSelectionListViewModel = ProjectSelectionListViewModel(
            feature: projectSelectionListComponent.projectSelectionListFeature(
                params: projectSelectionListParams
            )
        )

        let stackRouter = SwiftUIStackRouter()

        let projectSelectionListView = ProjectSelectionListView(
            viewModel: projectSelectionListViewModel,
            stackRouter: stackRouter
        )

        let hostingController = StyledHostingController(
            rootView: projectSelectionListView,
            appearance: .withoutBackButtonTitle
        )
        hostingController.hidesBottomBarWhenPushed = true
        hostingController.navigationItem.largeTitleDisplayMode = .never
        hostingController.title = Strings.ProjectSelectionList.title

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
