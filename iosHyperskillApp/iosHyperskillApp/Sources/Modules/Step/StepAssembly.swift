import shared
import SwiftUI

final class StepAssembly: Assembly, UIKitAssembly {
    private let stepRoute: StepRoute

    private weak var rootViewController: UIViewController?

    init(stepRoute: StepRoute, rootViewController: UIViewController? = nil) {
        self.stepRoute = stepRoute
        self.rootViewController = rootViewController
    }

    static func stageImplement(stepRoute: StepRoute, rootViewController: UIViewController?) -> Self {
        self.init(stepRoute: stepRoute, rootViewController: rootViewController)
    }

    func makeModule() -> StepView {
        let viewModel = makeViewModel()

        let stackRouter = SwiftUIStackRouter(rootViewController: rootViewController)
        let modalRouter = SwiftUIModalRouter(rootViewController: rootViewController)

        return StepView(
            viewModel: viewModel,
            stackRouter: stackRouter,
            modalRouter: modalRouter,
            panModalPresenter: PanModalPresenter()
        )
    }

    func makeModule() -> UIViewController {
        let viewModel = makeViewModel()

        let stackRouter = SwiftUIStackRouter()
        let modalRouter = SwiftUIModalRouter()

        let stepView = StepView(
            viewModel: viewModel,
            stackRouter: stackRouter,
            modalRouter: modalRouter,
            panModalPresenter: PanModalPresenter()
        )
        let hostingController = StyledHostingController(rootView: stepView, appearance: .withoutBackButtonTitle)
        // Fixes an issue with that SwiftUI view content layout unexpectedly pop/jumps on appear
        hostingController.navigationItem.largeTitleDisplayMode = .never

        modalRouter.rootViewController = hostingController
        stackRouter.rootViewController = hostingController

        return hostingController
    }

    private func makeViewModel() -> StepViewModel {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent
        let stepComponent = AppGraphBridge.sharedAppGraph.buildStepComponent(stepRoute: stepRoute)

        return StepViewModel(
            stepRoute: stepRoute,
            viewDataMapper: StepViewDataMapper(
                dateFormatter: commonComponent.dateFormatter,
                resourceProvider: commonComponent.resourceProvider,
                commentThreadTitleMapper: stepComponent.commentThreadTitleMapper
            ),
            feature: stepComponent.stepFeature
        )
    }
}
