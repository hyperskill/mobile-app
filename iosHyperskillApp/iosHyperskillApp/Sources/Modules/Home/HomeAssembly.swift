import SwiftUI

final class HomeAssembly: Assembly {
    func makeModule() -> HomeView {
        let homeComponent = AppGraphBridge.sharedAppGraph.buildHomeComponent()

        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent

        let viewModel = HomeViewModel(
            problemOfDayViewDataMapper: ProblemOfDayViewDataMapper(
                formatter: Formatter(resourceProvider: commonComponent.resourceProvider)
            ),
            feature: homeComponent.homeFeature
        )

        return HomeView(viewModel: viewModel)
    }
}
