import shared
import SwiftUI

final class StepAssembly: UIKitAssembly {
    private let stepRoute: StepRoute
    private let stackRouter: SwiftUIStackRouter

    init(stepRoute: StepRoute, stackRouter: SwiftUIStackRouter) {
        self.stepRoute = stepRoute
        self.stackRouter = stackRouter
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

        let modalRouter = SwiftUIModalRouter()
        let stepView = StepView(
            viewModel: viewModel,
            stackRouter: self.stackRouter,
            modalRouter: modalRouter,
            panModalPresenter: PanModalPresenter()
        )
        let hostingController = RemoveBackButtonTitleHostingController(rootView: stepView)

        modalRouter.rootViewController = hostingController

        return hostingController
    }
}
