import shared
import SwiftUI

final class StepQuizAssembly: Assembly {
    private let step: Step

    init(step: Step) {
        self.step = step
    }

    func makeModule() -> StepQuizView {
        let stepQuizComponent = AppGraphBridge.sharedAppGraph.buildStepQuizComponent()

        let notificationComponent = AppGraphBridge.sharedAppGraph.buildNotificationComponent()

        let viewDataMapper = StepQuizViewDataMapper(
            stepQuizStatsTextMapper: stepQuizComponent.stepQuizStatsTextMapper,
            stepQuizTitleMapper: stepQuizComponent.stepQuizTitleMapper
        )
        let viewModel = StepQuizViewModel(
            step: self.step,
            viewDataMapper: viewDataMapper,
            notificationService: NotificationsService(
                notificationInteractor: notificationComponent.notificationInteractor
            ),
            notificationsRegistrationService: .shared,
            feature: stepQuizComponent.stepQuizFeature
        )

        return StepQuizView(viewModel: viewModel)
    }
}
