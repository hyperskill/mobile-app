import shared
import SwiftUI

final class ProgressScreenAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let progressScreenComponent = AppGraphBridge.sharedAppGraph.buildProgressScreenComponent()

        let progressScreenViewModel = ProgressScreenViewModel(
            feature: progressScreenComponent.progressScreenFeature
        )
        let stackRouter = SwiftUIStackRouter()

        let progressScreenView = ProgressScreenView(
            viewModel: progressScreenViewModel,
            stackRouter: stackRouter
        )

        let hostingController = StyledHostingController(
            rootView: progressScreenView,
            appearance: .withoutBackButtonTitle
        )
        hostingController.navigationItem.largeTitleDisplayMode = .never
        hostingController.title = Strings.ProgressScreen.navigationTitle

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
