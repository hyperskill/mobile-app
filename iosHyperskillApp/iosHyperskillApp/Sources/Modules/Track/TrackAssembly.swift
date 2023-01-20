import SwiftUI

final class TrackAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent
        let trackComponent = AppGraphBridge.sharedAppGraph.buildTrackComponent()

        let viewModel = TrackViewModel(
            viewDataMapper: TrackViewDataMapper(
                formatter: Formatter(resourceProvider: commonComponent.resourceProvider)
            ),
            feature: trackComponent.trackFeature
        )

        let stackRouter = SwiftUIStackRouter()
        let trackView = TrackView(viewModel: viewModel, stackRouter: stackRouter)
        let hostingController = UIHostingController(rootView: trackView)

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
