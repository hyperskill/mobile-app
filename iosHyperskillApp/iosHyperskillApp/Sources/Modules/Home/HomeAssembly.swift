import shared
import SwiftUI

final class HomeAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let homeComponent = AppGraphBridge.sharedAppGraph.buildHomeComponent()

        let viewModel = HomeViewModel(
            feature: homeComponent.homeFeature
        )

        let stackRouter = SwiftUIStackRouter()
        let panModalPresenter = PanModalPresenter()

        let homeView = HomeView(
            viewModel: viewModel,
            stackRouter: stackRouter,
            panModalPresenter: panModalPresenter
        )
        let hostingController = StyledHostingController(
            rootView: homeView,
            appearance: .leftAlignedNavigationBarTitle
        )

        stackRouter.rootViewController = hostingController
        panModalPresenter.rootViewController = hostingController

        return hostingController
    }
}
