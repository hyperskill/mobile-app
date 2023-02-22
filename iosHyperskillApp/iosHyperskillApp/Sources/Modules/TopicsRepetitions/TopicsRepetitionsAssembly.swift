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

        let viewController = StyledHostingController(rootView: topicsRepetitionsView, appearance: .withoutBackButtonTitle)

        stackRouter.rootViewController = viewController

        return viewController
    }
}
