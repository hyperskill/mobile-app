import shared
import SwiftUI

extension UsersQuestionnaireOnboardingView {
    struct Appearance {
        let backgroundColor = Color(ColorPalette.newLayer1)
    }
}

struct UsersQuestionnaireOnboardingView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: UsersQuestionnaireOnboardingViewModel

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: appearance.backgroundColor)

            UsersQuestionnaireOnboardingContentView(
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
                onSkipButtotTap: viewModel.doSkip
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

// MARK: - UsersQuestionnaireOnboardingView (ViewAction) -

private extension UsersQuestionnaireOnboardingView {
    func handleViewAction(
        _ viewAction: LegacyUsersQuestionnaireOnboardingFeatureActionViewAction
    ) {
        switch LegacyUsersQuestionnaireOnboardingFeatureActionViewActionKs(viewAction) {
        case .showSendSuccessMessage(let showSendSuccessMessageViewAction):
            ProgressHUD.showSuccess(status: showSendSuccessMessageViewAction.message)
        case .completeUsersQuestionnaireOnboarding:
            viewModel.doCompleteOnboarding()
        }
    }
}
