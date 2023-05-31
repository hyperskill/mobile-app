import shared
import SwiftUI

final class ProjectSelectionListAssembly: UIKitAssembly {
    private let isNewUserMode: Bool
    private let trackID: Int64

    init(trackID: Int64, isNewUserMode: Bool) {
        self.trackID = trackID
        self.isNewUserMode = isNewUserMode
    }

    func makeModule() -> UIViewController {
        let projectSelectionListComponent = AppGraphBridge.sharedAppGraph.buildProjectSelectionListComponent()

        let viewModel = ProjectSelectionListViewModel(
            feature: projectSelectionListComponent.projectSelectionListFeature(
                params: ProjectSelectionListParams(
                    isNewUserMode: isNewUserMode,
                    trackId: trackID
                )
            )
        )

        let stackRouter = SwiftUIStackRouter()

        let rootView = ProjectSelectionListView(viewModel: viewModel, stackRouter: stackRouter)
        let hostingController = StyledHostingController(
            rootView: rootView,
            appearance: .withoutBackButtonTitle
        )
        hostingController.hidesBottomBarWhenPushed = true
        hostingController.navigationItem.largeTitleDisplayMode = .never
        hostingController.title = Strings.ProjectSelectionList.title

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
