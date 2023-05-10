import Darwin
import shared
import SwiftUI

final class DebugViewModel: FeatureViewModel<
  DebugFeatureViewState,
  DebugFeatureMessage,
  DebugFeatureActionViewAction
> {
    private let notificationsService: NotificationsService

    var stateKs: DebugFeatureViewStateKs { .init(state) }

    init(
        notificationsService: NotificationsService,
        feature: Presentation_reduxFeature
    ) {
        self.notificationsService = notificationsService
        super.init(feature: feature)
    }

    override func shouldNotifyStateDidChange(
        oldState: DebugFeatureViewState,
        newState: DebugFeatureViewState
    ) -> Bool {
        DebugFeatureViewStateKs(oldState) != DebugFeatureViewStateKs(newState)
    }

    func doLoadDebugSettings(forceUpdate: Bool = false) {
        onNewMessage(DebugFeatureMessageInitialize(forceUpdate: forceUpdate))
    }

    func doSelectEndpointConfig(_ endpointConfig: EndpointConfigType) {
        onNewMessage(DebugFeatureMessageSelectEndpointConfig(endpointConfig: endpointConfig))
    }

    func doStepNavigationInputChange(text: String) {
        onNewMessage(DebugFeatureMessageStepNavigationInputTextChanged(text: text))
    }

    func doStepNavigationOpenStep() {
        onNewMessage(DebugFeatureMessageStepNavigationOpenClicked())
    }

    func doStageImplementNavigationInputChange(projectIdText: String) {
        guard case .content(let debugFeatureViewStateContent) = stateKs else {
            return
        }

        onNewMessage(
            DebugFeatureMessageStageImplementNavigationInputChanged(
                projectId: projectIdText,
                stageId: debugFeatureViewStateContent.navigationInput.stageImplement.stageId
            )
        )
    }

    func doStageImplementNavigationInputChange(stageIdText: String) {
        guard case .content(let debugFeatureViewStateContent) = stateKs else {
            return
        }

        onNewMessage(
            DebugFeatureMessageStageImplementNavigationInputChanged(
                projectId: debugFeatureViewStateContent.navigationInput.stageImplement.projectId,
                stageId: stageIdText
            )
        )
    }

    func doStageImplementNavigationOpen() {
        onNewMessage(DebugFeatureMessageStageImplementNavigationOpenClicked())
    }

    func doApplySettings() {
        onNewMessage(DebugFeatureMessageApplySettingsClicked())
    }

    func doRestartApplication() {
        Task {
            await notificationsService.scheduleRestartApplicationLocalNotification()
            // Delay the task by 2 second
            try? await Task.sleep(nanoseconds: 2_000_000_000)
            exit(0)
        }
    }
}
