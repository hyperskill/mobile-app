import shared
import SwiftUI

final class StepQuizAssembly: Assembly {
    private let step: Step

    private let onQuizCompleted: (() -> Void)?

    init(step: Step, onQuizCompleted: (() -> Void)?) {
        self.step = step
        self.onQuizCompleted = onQuizCompleted
    }

    func makeModule() -> StepQuizView {
        let commonComponent = AppGraphBridge.sharedAppGraph.commonComponent
        let stepQuizComponent = AppGraphBridge.sharedAppGraph.buildStepQuizComponent()

        let notificationComponent = AppGraphBridge.sharedAppGraph.buildNotificationComponent()

        let viewDataMapper = StepQuizViewDataMapper(
            stepQuizStatsTextMapper: stepQuizComponent.stepQuizStatsTextMapper,
            stepQuizTitleMapper: stepQuizComponent.stepQuizTitleMapper
        )
        let viewModel = StepQuizViewModel(
            step: self.step,
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

        return StepQuizView(viewModel: viewModel, onQuizCompleted: onQuizCompleted)
    }
}
