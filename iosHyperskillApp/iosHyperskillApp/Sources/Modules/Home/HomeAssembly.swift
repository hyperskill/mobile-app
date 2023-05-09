import SwiftUI

final class HomeAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let homeComponent = AppGraphBridge.sharedAppGraph.buildHomeComponent()

        let viewModel = HomeViewModel(feature: homeComponent.homeFeature)

        let stackRouter = SwiftUIStackRouter()
        let homeView = HomeView(viewModel: viewModel, stackRouter: stackRouter)
        let hostingController = StyledHostingController(
            rootView: homeView,
            appearance: .leftAlignedNavigationBarTitle
        )

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
