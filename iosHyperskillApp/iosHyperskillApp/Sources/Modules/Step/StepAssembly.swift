import shared
import SwiftUI

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
        let hostingController = RemoveBackButtonTitleHostingController(rootView: stepView)

        modalRouter.rootViewController = hostingController
        stackRouter.rootViewController = hostingController

        return hostingController
    }
}
