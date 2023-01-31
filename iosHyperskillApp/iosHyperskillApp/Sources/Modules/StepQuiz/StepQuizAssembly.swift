import shared
import SwiftUI

final class StepQuizAssembly: Assembly {
    private let step: Step
    private let stepRoute: StepRoute
    private let provideModuleInputCallback: (StepQuizInputProtocol?) -> Void
    private weak var moduleOutput: StepQuizOutputProtocol?

    init(
        step: Step,
        stepRoute: StepRoute,
        provideModuleInputCallback: @escaping (StepQuizInputProtocol?) -> Void,
        moduleOutput: StepQuizOutputProtocol? = nil
    ) {
        self.step = step
        self.stepRoute = stepRoute
        self.provideModuleInputCallback = provideModuleInputCallback
        self.moduleOutput = moduleOutput
    }

    func makeModule() -> StepQuizView {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent
        let stepQuizComponent = AppGraphBridge.sharedAppGraph.buildStepQuizComponent(stepRoute: self.stepRoute)

        let notificationComponent = AppGraphBridge.sharedAppGraph.buildNotificationComponent()

        let viewDataMapper = StepQuizViewDataMapper(
            stepQuizStatsTextMapper: stepQuizComponent.stepQuizStatsTextMapper,
            stepQuizTitleMapper: stepQuizComponent.stepQuizTitleMapper
        )
        let viewModel = StepQuizViewModel(
            step: self.step,
            stepRoute: self.stepRoute,
            moduleOutput: self.moduleOutput,
            viewDataMapper: viewDataMapper,
            userPermissionRequestTextMapper: StepQuizUserPermissionRequestTextMapper(
                resourceProvider: commonComponent.resourceProvider
            ),
            notificationService: NotificationsService(
                notificationInteractor: notificationComponent.notificationInteractor
            ),
            notificationsRegistrationService: .shared,
            feature: stepQuizComponent.stepQuizFeature
        )

        provideModuleInputCallback(viewModel)

        return StepQuizView(viewModel: viewModel)
    }
}
