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

        let pushRouter = SwiftUIStackRouter()
        let trackView = TrackView(viewModel: viewModel, pushRouter: pushRouter)
        let hostingController = UIHostingController(rootView: trackView)

        pushRouter.rootViewController = hostingController

        return hostingController
    }
}
