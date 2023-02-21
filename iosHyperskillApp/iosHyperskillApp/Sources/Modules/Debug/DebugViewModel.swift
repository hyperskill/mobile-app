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

    func doApplySettings() {
        onNewMessage(DebugFeatureMessageApplySettingsClicked())
    }

    func doRestartApplication() {
        Task {
            await notificationsService.scheduleRestartApplicationLocalNotification()
            exit(0)
        }
    }
}
