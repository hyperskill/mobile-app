import Foundation
import shared

final class QuestionnaireOnboardingViewModel: FeatureViewModel<
  QuestionnaireOnboardingFeature.ViewState,
  QuestionnaireOnboardingFeatureMessage,
  QuestionnaireOnboardingFeatureActionViewAction
> {
    weak var moduleOutput: QuestionnaireOnboardingOutputProtocol?

    override func shouldNotifyStateDidChange(
        oldState: QuestionnaireOnboardingFeature.ViewState,
        newState: QuestionnaireOnboardingFeature.ViewState
    ) -> Bool {
        !oldState.isEqual(newState)
    }

    func selectChoice(_ choice: String) {
        onNewMessage(QuestionnaireOnboardingFeatureMessageClickedChoice(choice: choice))
    }

    func doTextInputValueChanged(_ value: String) {
        onNewMessage(QuestionnaireOnboardingFeatureMessageTextInputValueChanged(text: value))
    }

    func doSend() {
        onNewMessage(QuestionnaireOnboardingFeatureMessageSendButtonClicked())
    }

    func doSkip() {
        onNewMessage(QuestionnaireOnboardingFeatureMessageSkipButtonClicked())
    }

    func doCompleteOnboarding() {
        moduleOutput?.handleQuestionnaireOnboardingCompleted()
    }

    func logViewedEvent() {
        onNewMessage(QuestionnaireOnboardingFeatureMessageViewedEventMessage())
    }
}
