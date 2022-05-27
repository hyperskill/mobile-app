import shared
import SwiftUI

final class AppAssembly: Assembly {
    func makeModule() -> AppView {
        let feature = AppGraphBridge.shared.mainComponentManual.appFeature
        let viewModel = AppViewModel(feature: feature)
        return AppView(viewModel: viewModel)
    }
}
