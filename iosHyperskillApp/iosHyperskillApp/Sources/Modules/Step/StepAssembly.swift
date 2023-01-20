import shared
import SwiftUI

final class StepAssembly: UIKitAssembly {
    private let stepRoute: StepRoute
    private let moduleOutput: StepOutputProtocol?

    init(stepRoute: StepRoute, moduleOutput: StepOutputProtocol? = nil) {
        self.stepRoute = stepRoute
        self.moduleOutput = moduleOutput
    }

    func makeModule() -> UIViewController {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent
        let stepComponent = AppGraphBridge.sharedAppGraph.buildStepComponent()

        let viewModel = StepViewModel(
            stepRoute: self.stepRoute,
            moduleOutput: self.moduleOutput,
            viewDataMapper: StepViewDataMapper(
                formatter: Formatter(resourceProvider: commonComponent.resourceProvider),
                resourceProvider: commonComponent.resourceProvider,
                commentThreadTitleMapper: stepComponent.commentThreadTitleMapper
            ),
            feature: stepComponent.stepFeature
        )

        let pushRouter = SwiftUIStackRouter()
        let modalRouter = SwiftUIModalRouter()
        let stepView = StepView(
            viewModel: viewModel,
            pushRouter: pushRouter,
            modalRouter: modalRouter
        )
        let hostingController = RemoveBackButtonTitleHostingController(rootView: stepView)

        pushRouter.rootViewController = hostingController
        modalRouter.rootViewController = hostingController

        return hostingController
    }
}
