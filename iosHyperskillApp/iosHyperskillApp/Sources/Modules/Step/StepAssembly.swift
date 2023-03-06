import shared
import SwiftUI

final class StepSwiftUIAssembly: Assembly {
    private let stepRoute: StepRoute

    private weak var rootViewController: UIViewController?

    init(stepRoute: StepRoute, rootViewController: UIViewController?) {
        self.stepRoute = stepRoute
        self.rootViewController = rootViewController
    }

    func makeModule() -> StepView {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent
        let stepComponent = AppGraphBridge.sharedAppGraph.buildStepComponent(stepRoute: stepRoute)

        let viewModel = StepViewModel(
            stepRoute: stepRoute,
            viewDataMapper: StepViewDataMapper(
                formatter: Formatter(resourceProvider: commonComponent.resourceProvider),
                resourceProvider: commonComponent.resourceProvider,
                commentThreadTitleMapper: stepComponent.commentThreadTitleMapper
            ),
            feature: stepComponent.stepFeature
        )

        let stackRouter = SwiftUIStackRouter(rootViewController: rootViewController)
        let modalRouter = SwiftUIModalRouter(rootViewController: rootViewController)

        return StepView(
            viewModel: viewModel,
            stackRouter: stackRouter,
            modalRouter: modalRouter,
            panModalPresenter: PanModalPresenter()
        )
    }
}

final class StepAssembly: UIKitAssembly {
    private let stepRoute: StepRoute

    init(stepRoute: StepRoute) {
        self.stepRoute = stepRoute
    }

    func makeModule() -> UIViewController {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent
        let stepComponent = AppGraphBridge.sharedAppGraph.buildStepComponent(stepRoute: stepRoute)

        let viewModel = StepViewModel(
            stepRoute: self.stepRoute,
            viewDataMapper: StepViewDataMapper(
                formatter: Formatter(resourceProvider: commonComponent.resourceProvider),
                resourceProvider: commonComponent.resourceProvider,
                commentThreadTitleMapper: stepComponent.commentThreadTitleMapper
            ),
            feature: stepComponent.stepFeature
        )

        let stackRouter = SwiftUIStackRouter()
        let modalRouter = SwiftUIModalRouter()
        let stepView = StepView(
            viewModel: viewModel,
            stackRouter: stackRouter,
            modalRouter: modalRouter,
            panModalPresenter: PanModalPresenter()
        )
        let hostingController = StyledHostingController(rootView: stepView, appearance: .withoutBackButtonTitle)

        modalRouter.rootViewController = hostingController
        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
