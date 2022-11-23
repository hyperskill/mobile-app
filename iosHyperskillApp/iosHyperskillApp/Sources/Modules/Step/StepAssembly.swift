import shared
import SwiftUI

final class StepAssembly: UIKitAssembly {
    private let stepID: Int

    private let onQuizCompleted: (() -> Void)?

    init(stepID: Int, onQuizCompleted: (() -> Void)? = nil) {
        self.stepID = stepID
        self.onQuizCompleted = onQuizCompleted
    }

    func makeModule() -> UIViewController {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent
        let stepComponent = AppGraphBridge.sharedAppGraph.buildStepComponent()

        let viewModel = StepViewModel(
            stepID: self.stepID,
            viewDataMapper: StepViewDataMapper(
                formatter: Formatter(resourceProvider: commonComponent.resourceProvider),
                resourceProvider: commonComponent.resourceProvider,
                commentThreadTitleMapper: stepComponent.commentThreadTitleMapper
            ),
            feature: stepComponent.stepFeature
        )

        let pushRouter = SwiftUIPushRouter()
        let modalRouter = SwiftUIModalRouter()
        let stepView = StepView(
            viewModel: viewModel,
            pushRouter: pushRouter,
            modalRouter: modalRouter,
            onQuizCompleted: onQuizCompleted
        )
        let hostingController = RemoveBackButtonTitleHostingController(rootView: AnyView(stepView))

        pushRouter.rootViewController = hostingController
        modalRouter.rootViewController = hostingController

        return hostingController
    }
}
