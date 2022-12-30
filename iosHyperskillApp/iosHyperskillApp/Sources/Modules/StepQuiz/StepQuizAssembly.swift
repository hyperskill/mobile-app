import shared
import SwiftUI

final class StepQuizAssembly: Assembly {
    private let step: Step
    private let stepRoute: StepRoute

    init(step: Step, stepRoute: StepRoute) {
        self.step = step
        self.stepRoute = stepRoute
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

        return StepQuizView(viewModel: viewModel)
    }
}
