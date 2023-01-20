import shared
import SwiftUI

class TopicsRepetitionsAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let topicsRepetitionsComponent = AppGraphBridge.sharedAppGraph.buildTopicsRepetitionsComponent()

        let viewModel = TopicsRepetitionsViewModel(feature: topicsRepetitionsComponent.topicsRepetitionsFeature)

        let pushRouter = SwiftUIStackRouter()

        let topicsRepetitionsView = TopicsRepetitionsView(
            viewModel: viewModel,
            pushRouter: pushRouter,
            dataMapper: topicsRepetitionsComponent.topicsRepetitionsViewDataMapper
        )

        let viewController = RemoveBackButtonTitleHostingController(rootView: topicsRepetitionsView)

        pushRouter.rootViewController = viewController

        return viewController
    }
}
