import SwiftUI

final class HomeAssembly: Assembly {
    func makeModule() -> HomeView {
        let homeComponent = AppGraphBridge.sharedAppGraph.buildHomeComponent()

        let viewModel = HomeViewModel(feature: homeComponent.homeFeature)

        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent

        return HomeView(formatter: Formatter(resourceProvider: commonComponent.resourceProvider), viewModel: viewModel)
    }
}
