import shared
import SwiftUI

final class LeaderboardAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let leaderboardScreenComponent = AppGraphBridge.sharedAppGraph.buildLeaderboardScreenComponent()

        let leaderboardViewModel = LeaderboardViewModel(
            feature: leaderboardScreenComponent.leaderboardScreenFeature
        )

        let stackRouter = StackRouter()
        let panModalPresenter = PanModalPresenter()

        let leaderboardView = LeaderboardView(
            viewModel: leaderboardViewModel,
            stackRouter: stackRouter,
            panModalPresenter: panModalPresenter
        )

        let hostingController = StyledHostingController(
            rootView: leaderboardView,
            appearance: .leftAlignedNavigationBarTitle
        )
        hostingController.title = Strings.Leaderboard.title

        stackRouter.rootViewController = hostingController
        panModalPresenter.rootViewController = hostingController

        return hostingController
    }
}
