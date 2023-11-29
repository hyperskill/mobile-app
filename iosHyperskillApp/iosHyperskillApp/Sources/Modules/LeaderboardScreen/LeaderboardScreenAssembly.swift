import shared
import SwiftUI

final class LeaderboardScreenAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let leaderboardScreenComponent = AppGraphBridge.sharedAppGraph.buildLeaderboardScreenComponent()

        let leaderboardScreenViewModel = LeaderboardScreenViewModel(
            feature: leaderboardScreenComponent.leaderboardScreenFeature
        )

        let leaderboardScreenView = LeaderboardScreenView(
            viewModel: leaderboardScreenViewModel
        )

        let hostingController = StyledHostingController(
            rootView: leaderboardScreenView
        )
        hostingController.navigationItem.largeTitleDisplayMode = .never

        return hostingController
    }
}
