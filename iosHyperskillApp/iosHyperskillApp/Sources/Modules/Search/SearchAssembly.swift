import shared
import SwiftUI

@available(iOS 15.0, *)
final class SearchAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let searchComponent = AppGraphBridge.sharedAppGraph.buildSearchComponent()

        let searchViewModel = SearchViewModel(
            feature: searchComponent.searchFeature
        )

        let stackRouter = StackRouter()

        let searchView = SearchView(
            viewModel: searchViewModel,
            stackRouter: stackRouter
        )

        let hostingController = StyledHostingController(
            rootView: searchView
        )
        hostingController.hidesBottomBarWhenPushed = true
        hostingController.navigationItem.largeTitleDisplayMode = .always
        hostingController.title = Strings.Search.title

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
