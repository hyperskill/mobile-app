import shared
import SwiftUI

extension AuthEmailView {
    struct Appearance {
        let logoSize: CGFloat = 48

        let continueWithSocialButtonInsets = LayoutInsets(top: 24)

        let keyboardDistanceFromTextField: CGFloat = 60
    }
}

struct AuthEmailView: View {
    let appearance: Appearance

    @ObservedObject private var viewModel: AuthEmailViewModel

    @ObservedObject private var navigationState: AppNavigationState

    @Environment(\.presentationMode) private var presentationMode

    @State private var emailText = ""
    @State private var passwordText = ""

    init(viewModel: AuthEmailViewModel, navigationState: AppNavigationState, appearance: Appearance = Appearance()) {
        self.viewModel = viewModel
        self.navigationState = navigationState
        self.appearance = appearance
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        let formState = viewModel.state.formState

        if formState is AuthCredentialsFeatureFormStateEditing {
            ProgressHUD.dismiss()
        } else if formState is AuthCredentialsFeatureFormStateLoading {
            ProgressHUD.show()
        } else if formState is AuthCredentialsFeatureFormStateError {
            ProgressHUD.dismiss()
        }

        let formErrorMessage = (viewModel.state.formState as? AuthCredentialsFeatureFormStateError)?.error

        return AuthAdaptiveContentView { horizontalSizeClass in
            AuthLogoView(logoWidthHeight: appearance.logoSize)
                .padding(horizontalSizeClass == .regular ? .bottom : .vertical, appearance.logoSize)

            AuthEmailFormView(
                emailText: $emailText,
                passwordText: $passwordText,
                errorMessage: formErrorMessage,
                onLogIn: viewModel.doLogIn,
                onResetPassword: viewModel.doResetPassword
            )
            .onChange(of: emailText) { _ in viewModel.doFormInputChange(email: emailText, password: passwordText) }
            .onChange(of: passwordText) { _ in viewModel.doFormInputChange(email: emailText, password: passwordText) }
            .disabled(formState is AuthCredentialsFeatureFormStateLoading)

            Button(Strings.authEmailSocialText, action: { presentationMode.wrappedValue.dismiss() })
                .buttonStyle(OutlineButtonStyle(style: .violet))
                .padding(appearance.continueWithSocialButtonInsets.edgeInsets)
        }
        .onAppear {
            viewModel.startListening()
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
        case is AuthCredentialsFeatureActionViewActionNavigateToHomeScreen:
            ProgressHUD.showSuccess()
            withAnimation {
                navigationState.presentingAuthScreen = false
            }
        default:
            print("AuthEmailView :: unhandled viewAction = \(viewAction)")
        }
    }
}

struct AuthEmailView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            AuthEmailAssembly().makeModule()
                .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))

            AuthEmailAssembly().makeModule()
                .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))
                .preferredColorScheme(.dark)

            AuthEmailAssembly().makeModule()
                .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
        }
    }
}
