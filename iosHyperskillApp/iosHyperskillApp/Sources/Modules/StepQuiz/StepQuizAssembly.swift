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

        let problemsLimitComponent = AppGraphBridge.sharedAppGraph.buildProblemsLimitComponent(
            screen: ProblemsLimitScreen.stepQuiz
        )

        let viewModel = StepQuizViewModel(
            step: step,
            stepRoute: stepRoute,
            moduleOutput: moduleOutput,
            provideModuleInputCallback: provideModuleInputCallback,
            viewDataMapper: viewDataMapper,
            userPermissionRequestTextMapper: StepQuizUserPermissionRequestTextMapper(
                resourceProvider: commonComponent.resourceProvider
            ),
            problemsLimitViewStateMapper: problemsLimitComponent.problemsLimitViewStateMapper,
            notificationService: NotificationsService(
                notificationInteractor: notificationComponent.notificationInteractor
            ),
            notificationsRegistrationService: .shared,
            feature: stepQuizComponent.stepQuizFeature
        )

        return StepQuizView(viewModel: viewModel)
    }
}
