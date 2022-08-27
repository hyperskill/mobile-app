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

        if formState is AuthCredentialsFeatureFormStateEditing {
            ProgressHUD.dismiss()
        } else if formState is AuthCredentialsFeatureFormStateLoading {
            ProgressHUD.show()
        } else if formState is AuthCredentialsFeatureFormStateError {
            ProgressHUD.dismiss()
        }

        return AuthAdaptiveContentView { horizontalSizeClass in
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
            .buttonStyle(OutlineButtonStyle(style: .violet))
            .padding(appearance.continueWithSocialButtonInsets.edgeInsets)
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)

            viewModel.logViewedEvent()

            KeyboardManager.setKeyboardDistanceFromTextField(appearance.keyboardDistanceFromTextField)
        }
        .onDisappear {
            viewModel.stopListening()
            KeyboardManager.setDefaultKeyboardDistanceFromTextField()
        }
        .navigationBarHidden(true)
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: AuthCredentialsFeatureActionViewAction) {
        switch viewAction {
        case let completeAuthFlowViewAction as AuthCredentialsFeatureActionViewActionCompleteAuthFlow:
            ProgressHUD.showSuccess()
            viewModel.doCompleteAuthFlow(isNewUser: completeAuthFlowViewAction.isNewUser)
        default:
            print("AuthEmailView :: unhandled viewAction = \(viewAction)")
        }
    }
}

struct AuthCredentialsView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            AuthCredentialsAssembly().makeModule()
                .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))

            AuthCredentialsAssembly().makeModule()
                .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))
                .preferredColorScheme(.dark)

            AuthCredentialsAssembly().makeModule()
                .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
        }
    }
}
