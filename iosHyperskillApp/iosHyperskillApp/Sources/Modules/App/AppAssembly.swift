import SwiftUI
import shared

final class AppAssembly: Assembly {
    func makeModule() -> AppView {
        let appFeature = AppFeatureBuilder.shared.build()

        let appViewModel = AppViewModel(feature: appFeature)

        return AppView(viewModel: appViewModel)
    }
}
