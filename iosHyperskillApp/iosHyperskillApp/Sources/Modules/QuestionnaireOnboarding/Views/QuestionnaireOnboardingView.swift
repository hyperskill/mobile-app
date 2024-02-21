import shared
import SwiftUI

extension QuestionnaireOnboardingView {
    struct Appearance {
        let backgroundColor = Color(ColorPalette.newLayer1)
    }
}

struct QuestionnaireOnboardingView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: QuestionnaireOnboardingViewModel

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: appearance.backgroundColor)

            QuestionnaireOnboardingContentView(
                title: viewModel.state.title,
                choices: viewModel.state.choices,
                selectedChoice: viewModel.state.selectedChoice,
                onChoiceTap: viewModel.selectChoice(_:),
                textInputValue: Binding<String>(
                    get: { viewModel.state.textInputValue ?? "" },
                    set: { viewModel.doTextInputValueChanged($0) }
                ),
                isTextInputVisible: viewModel.state.isTextInputVisible,
                isSendButtonEnabled: viewModel.state.isSendButtonEnabled,
                onSendButtotTap: viewModel.doSend,
                onSkipButtotTap: viewModel.doSkip,
                isKeyboardVisible: viewModel.isKeyboardVisible
            )
            .animation(.default, value: viewModel.state)
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear {
            viewModel.stopListening()
            viewModel.onViewAction = nil
        }
    }
}

// MARK: - QuestionnaireOnboardingView (ViewAction) -

private extension QuestionnaireOnboardingView {
    func handleViewAction(
        _ viewAction: QuestionnaireOnboardingFeatureActionViewAction
    ) {
        switch QuestionnaireOnboardingFeatureActionViewActionKs(viewAction) {
        case .showSendSuccessMessage(let showSendSuccessMessageViewAction):
            ProgressHUD.showSuccess(status: showSendSuccessMessageViewAction.message)
        case .completeQuestionnaireOnboarding:
            viewModel.doCompleteOnboarding()
        }
    }
}
