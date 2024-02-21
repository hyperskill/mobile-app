import Foundation
import shared

final class QuestionnaireOnboardingViewModel: FeatureViewModel<
  QuestionnaireOnboardingFeature.ViewState,
  QuestionnaireOnboardingFeatureMessage,
  QuestionnaireOnboardingFeatureActionViewAction
> {
    weak var moduleOutput: QuestionnaireOnboardingOutputProtocol?

    private(set) var isKeyboardVisible = false {
        didSet {
            guard oldValue != isKeyboardVisible else {
                return
            }

            mainScheduler.schedule {
                self.objectWillChange.send()
            }
        }
    }

    init(feature: Presentation_reduxFeature) {
        super.init(feature: feature)

        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleKeyboardWillShowNotification),
            name: UIResponder.keyboardWillShowNotification,
            object: nil
        )
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleKeyboardWillHideNotification),
            name: UIResponder.keyboardWillHideNotification,
            object: nil
        )
    }

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

    @objc
    private func handleKeyboardWillShowNotification() {
        isKeyboardVisible = true
    }

    @objc
    private func handleKeyboardWillHideNotification() {
        isKeyboardVisible = false
    }
}
