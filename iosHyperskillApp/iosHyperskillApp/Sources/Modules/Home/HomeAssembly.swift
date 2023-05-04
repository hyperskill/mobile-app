import shared
import SwiftUI

final class HomeAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let homeComponent = AppGraphBridge.sharedAppGraph.buildHomeComponent()

        let problemsLimitComponent = AppGraphBridge.sharedAppGraph.buildProblemsLimitComponent(
            screen: ProblemsLimitScreen.home
        )

        let viewModel = HomeViewModel(
            problemsLimitViewStateMapper: problemsLimitComponent.problemsLimitViewStateMapper,
            feature: homeComponent.homeFeature
        )

        let stackRouter = SwiftUIStackRouter()
        let homeView = HomeView(viewModel: viewModel, stackRouter: stackRouter)
        let hostingController = UIHostingController(rootView: homeView)

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
