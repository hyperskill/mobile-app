import Foundation
import shared

final class UsersQuestionnaireOnboardingViewModel: FeatureViewModel<
  LegacyUsersQuestionnaireOnboardingFeature.ViewState,
  LegacyUsersQuestionnaireOnboardingFeatureMessage,
  LegacyUsersQuestionnaireOnboardingFeatureActionViewAction
> {
    weak var moduleOutput: UsersQuestionnaireOnboardingOutputProtocol?

    override func shouldNotifyStateDidChange(
        oldState: LegacyUsersQuestionnaireOnboardingFeature.ViewState,
        newState: LegacyUsersQuestionnaireOnboardingFeature.ViewState
    ) -> Bool {
        !oldState.isEqual(newState)
    }

    func selectChoice(_ choice: String) {
        onNewMessage(LegacyUsersQuestionnaireOnboardingFeatureMessageClickedChoice(choice: choice))
    }

    func doTextInputValueChanged(_ value: String) {
        onNewMessage(LegacyUsersQuestionnaireOnboardingFeatureMessageTextInputValueChanged(text: value))
    }

    func doSend() {
        onNewMessage(LegacyUsersQuestionnaireOnboardingFeatureMessageSendButtonClicked())
    }

    func doSkip() {
        onNewMessage(LegacyUsersQuestionnaireOnboardingFeatureMessageSkipButtonClicked())
    }

    func doCompleteOnboarding() {
        moduleOutput?.handleUsersQuestionnaireOnboardingCompleted()
    }

    func logViewedEvent() {
        onNewMessage(LegacyUsersQuestionnaireOnboardingFeatureMessageViewedEventMessage())
    }
}
