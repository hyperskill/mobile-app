import shared
import SwiftUI

final class AppAssembly: Assembly {
    func makeModule() -> AppView {
        let appFeature = AppFeatureBuilder.shared.build(authInteractor: .default)
        let appViewModel = AppViewModel(feature: appFeature)
        return AppView(viewModel: appViewModel)
    }
}
