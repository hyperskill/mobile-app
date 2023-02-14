import shared
import SwiftUI

final class DebugAssembly: UIKitAssembly {
    func makeModule() -> UIViewController {
        let debugComponent = AppGraphBridge.sharedAppGraph.buildDebugComponent()

        let viewModel = DebugViewModel(
            notificationsService: NotificationsService(),
            feature: debugComponent.debugFeature
        )

        let stackRouter = SwiftUIStackRouter()
        let debugView = DebugView(viewModel: viewModel, stackRouter: stackRouter)
        let hostingController = UIHostingController(rootView: debugView)

        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
