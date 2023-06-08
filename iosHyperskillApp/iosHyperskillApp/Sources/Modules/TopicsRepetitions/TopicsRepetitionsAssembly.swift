import shared
import SwiftUI

class TopicsRepetitionsAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let topicsRepetitionsComponent = AppGraphBridge.sharedAppGraph.buildTopicsRepetitionsComponent()

        let viewModel = TopicsRepetitionsViewModel(feature: topicsRepetitionsComponent.topicsRepetitionsFeature)

        let stackRouter = SwiftUIStackRouter()

        let topicsRepetitionsView = TopicsRepetitionsView(
            viewModel: viewModel,
            stackRouter: stackRouter,
            dataMapper: topicsRepetitionsComponent.topicsRepetitionsViewDataMapper
        )

        let hostingController = StyledHostingController(
            rootView: topicsRepetitionsView,
            appearance: .withoutBackButtonTitle
        )
        // Fixes an issue with that SwiftUI view content layout unexpectedly pop/jumps on appear
        hostingController.navigationItem.largeTitleDisplayMode = .never

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
