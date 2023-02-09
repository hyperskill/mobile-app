import shared
import SwiftUI

final class DebugViewModel: FeatureViewModel<
  DebugFeatureViewState,
  DebugFeatureMessage,
  DebugFeatureActionViewAction
> {
    var stateKs: DebugFeatureViewStateKs { .init(state) }

    override func shouldNotifyStateDidChange(
        oldState: DebugFeatureViewState,
        newState: DebugFeatureViewState
    ) -> Bool {
        DebugFeatureViewStateKs(oldState) != DebugFeatureViewStateKs(newState)
    }

    func doLoadDebugSettings(forceUpdate: Bool = false) {
        onNewMessage(DebugFeatureMessageInitialize(forceUpdate: forceUpdate))
    }

    func doSelectEndpointConfig(_ endpointConfigType: EndpointConfigType) {
        onNewMessage(DebugFeatureMessageSelectEndpointConfig(endpointConfigType: endpointConfigType))
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
}
