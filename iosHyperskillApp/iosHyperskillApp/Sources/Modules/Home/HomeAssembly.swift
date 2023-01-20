import SwiftUI

final class HomeAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let homeComponent = AppGraphBridge.sharedAppGraph.buildHomeComponent()

        let viewModel = HomeViewModel(feature: homeComponent.homeFeature)

        let pushRouter = SwiftUIStackRouter()
        let homeView = HomeView(viewModel: viewModel, pushRouter: pushRouter)
        let hostingController = UIHostingController(rootView: homeView)

        pushRouter.rootViewController = hostingController

        return hostingController
    }
}
