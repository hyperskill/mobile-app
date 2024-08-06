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

        let stackRouter = StackRouter()
        let panModalPresenter = PanModalPresenter()

        let stageImplementView = StageImplementView(
            viewModel: stageImplementViewModel,
            stackRouter: stackRouter,
            panModalPresenter: panModalPresenter
        )

        let hostingController = StyledHostingController(
            rootView: stageImplementView,
            appearance: .withoutBackButtonTitle
        )
        // Fixes an issue with that SwiftUI view content layout unexpectedly pop/jumps on appear
        hostingController.navigationItem.largeTitleDisplayMode = .never

        stackRouter.rootViewController = hostingController
        panModalPresenter.rootViewController = hostingController

        return hostingController
    }
}
