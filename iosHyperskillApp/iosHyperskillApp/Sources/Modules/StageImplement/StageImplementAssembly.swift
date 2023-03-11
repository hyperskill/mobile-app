import shared
import SwiftUI

final class StageImplementAssembly: UIKitAssembly {
    private let projectID: Int64
    private let stageID: Int64

    init(projectID: Int64, stageID: Int64) {
        self.projectID = projectID
        self.stageID = stageID
    }

    func makeModule() -> UIViewController {
        let stageImplementComponent = AppGraphBridge.sharedAppGraph.buildStageImplementComponent(
            projectId: projectID,
            stageId: stageID
        )

        let stageImplementViewModel = StageImplementViewModel(
            projectID: projectID,
            stageID: stageID,
            feature: stageImplementComponent.stageImplementFeature
        )

        let stackRouter = SwiftUIStackRouter()

        let stageImplementView = StageImplementView(
            viewModel: stageImplementViewModel,
            stackRouter: stackRouter
        )

        let hostingController = StyledHostingController(
            rootView: stageImplementView,
            appearance: .withoutBackButtonTitle
        )

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
