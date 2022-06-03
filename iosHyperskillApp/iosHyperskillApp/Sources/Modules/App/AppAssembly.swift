import shared
import SwiftUI

final class AppAssembly: Assembly {
    func makeModule() -> AppView {
        let feature = AppGraphBridge.sharedAppGraph.mainComponent.appFeature
        let viewModel = AppViewModel(feature: feature)
        return AppView(
            viewModel: viewModel,
            panModalPresenter: PanModalPresenter(sourcelessRouter: SourcelessRouter())
        )
    }
}
