import Foundation
import shared

final class UsersQuestionnaireOnboardingViewModel: FeatureViewModel<
  UsersQuestionnaireOnboardingFeature.ViewState,
  UsersQuestionnaireOnboardingFeatureMessage,
  UsersQuestionnaireOnboardingFeatureActionViewAction
> {
    weak var moduleOutput: UsersQuestionnaireOnboardingOutputProtocol?

    override func shouldNotifyStateDidChange(
        oldState: UsersQuestionnaireOnboardingFeature.ViewState,
        newState: UsersQuestionnaireOnboardingFeature.ViewState
    ) -> Bool {
        !oldState.isEqual(newState)
    }

    func selectChoice(_ choice: String) {
        onNewMessage(UsersQuestionnaireOnboardingFeatureMessageClickedChoice(choice: choice))
    }

    func doTextInputValueChanged(_ value: String) {
        onNewMessage(UsersQuestionnaireOnboardingFeatureMessageTextInputValueChanged(text: value))
    }

    func doSend() {
        onNewMessage(UsersQuestionnaireOnboardingFeatureMessageSendButtonClicked())
    }

    func doSkip() {
        onNewMessage(UsersQuestionnaireOnboardingFeatureMessageSkipButtonClicked())
    }

    func doCompleteOnboarding() {
        moduleOutput?.handleUsersQuestionnaireOnboardingCompleted()
    }

    func logViewedEvent() {
        onNewMessage(UsersQuestionnaireOnboardingFeatureMessageViewedEventMessage())
    }
}
