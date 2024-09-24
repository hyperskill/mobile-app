import shared
import SwiftUI

extension AuthCredentialsView {
    struct Appearance {
        let logoSize: CGFloat = 48

        let continueWithSocialButtonInsets = LayoutInsets(top: 24)

        let keyboardDistanceFromTextField: CGFloat = 60
    }
}

struct AuthCredentialsView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: AuthCredentialsViewModel

    @Environment(\.presentationMode) private var presentationMode

    @State private var emailText = ""
    @State private var passwordText = ""

    var body: some View {
        let formState = viewModel.state.formState

        if (formState is AuthCredentialsFeatureFormStateLoading) || viewModel.state.isLoadingMagicLink {
            ProgressHUD.show()
        } else {
            ProgressHUD.dismiss()
        }

        return AuthAdaptiveContentView(
            onViewDidAppear: viewModel.logViewedEvent
        ) { horizontalSizeClass in
            AuthLogoView(logoWidthHeight: appearance.logoSize)
                .padding(horizontalSizeClass == .regular ? .bottom : .vertical, appearance.logoSize)

            AuthCredentialsFormView(
                emailText: $emailText,
                passwordText: $passwordText,
                errorMessage: viewModel.formErrorMessage,
                onLogIn: viewModel.doLogIn,
                onResetPassword: viewModel.doResetPassword
            )
            .onChange(of: emailText) { _ in viewModel.doFormInputChange(email: emailText, password: passwordText) }
            .onChange(of: passwordText) { _ in viewModel.doFormInputChange(email: emailText, password: passwordText) }
            .disabled(formState is AuthCredentialsFeatureFormStateLoading)

            Button(
                Strings.Auth.Credentials.socialText,
                action: {
                    viewModel.logClickedContinueWithSocialEvent()
                    presentationMode.wrappedValue.dismiss()
                }
            )
            .padding(appearance.continueWithSocialButtonInsets.edgeInsets)
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)

            KeyboardManager.setKeyboardDistanceFromTextField(appearance.keyboardDistanceFromTextField)
        }
        .onDisappear {
            viewModel.stopListening()
            viewModel.onViewAction = nil

            KeyboardManager.setDefaultKeyboardDistanceFromTextField()
        }
        .navigationBarHidden(true)
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: AuthCredentialsFeatureActionViewAction) {
        switch AuthCredentialsFeatureActionViewActionKs(viewAction) {
        case .completeAuthFlow(let data):
            ProgressHUD.showSuccess()
            viewModel.doCompleteAuthFlow(profile: data.profile)
        case .openUrl(let data):
            ProgressHUD.showSuccess()
            WebControllerManager.shared.presentWebControllerWithURLString(data.url)
        case .showGetMagicLinkError:
            ProgressHUD.showError()
        }
    }
}

#if DEBUG
#Preview {
    AuthCredentialsAssembly().makeModule()
}
#endif
