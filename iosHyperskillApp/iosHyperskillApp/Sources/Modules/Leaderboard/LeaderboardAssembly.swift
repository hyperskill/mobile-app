import shared
import SwiftUI

final class LeaderboardAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let leaderboardScreenComponent = AppGraphBridge.sharedAppGraph.buildLeaderboardScreenComponent()

        let leaderboardViewModel = LeaderboardViewModel(
            feature: leaderboardScreenComponent.leaderboardScreenFeature
        )

        let leaderboardView = LeaderboardView(
            viewModel: leaderboardViewModel
        )

        let hostingController = StyledHostingController(
            rootView: leaderboardView,
            appearance: .leftAlignedNavigationBarTitle
        )
        hostingController.title = Strings.Leaderboard.title

        return hostingController
    }
}
