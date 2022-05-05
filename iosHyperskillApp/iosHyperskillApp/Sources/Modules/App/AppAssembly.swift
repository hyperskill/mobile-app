import shared
import SwiftUI

final class AppAssembly: Assembly {
    func makeModule() -> AppView {
        let feature = AppFeatureBuilder.shared.build(authInteractor: .default)
        let viewModel = AppViewModel(feature: feature)
        return AppView(viewModel: viewModel)
    }
}
