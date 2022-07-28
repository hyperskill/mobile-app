import SwiftUI

final class HomeAssembly: Assembly {
    func makeModule() -> HomeView {
        let homeComponent = AppGraphBridge.sharedAppGraph.buildHomeComponent()

        let viewModel = HomeViewModel(feature: homeComponent.homeFeature)

        return HomeView(viewModel: viewModel)
    }
}
