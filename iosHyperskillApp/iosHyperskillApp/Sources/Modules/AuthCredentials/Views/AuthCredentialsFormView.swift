import SwiftUI

extension AuthCredentialsFormView {
    struct Appearance {
        let stackSpacing = LayoutInsets.defaultInset

        let textFieldHeight: CGFloat = 44

        let resetPasswordButtonInset = LayoutInsets.smallInset

        let backgroundColor = Color(UIColor.dynamic(light: ColorPalette.surface, dark: .secondarySystemBackground))

        let borderColor = Color(UIColor.dynamic(light: ColorPalette.brown, dark: .separator))
    }
}

struct AuthCredentialsFormView: View {
    private(set) var appearance = Appearance()

    @Binding var emailText: String

    @Binding var passwordText: String
    @State private var passwordFirstResponderAction: TextFieldWrapper.FirstResponderAction?

    var errorMessage: String?

    var onLogIn: (() -> Void)?

    var onResetPassword: (() -> Void)?

    private var isInputFulfilled: Bool {
        !emailText.trimmed().isEmpty && !passwordText.trimmed().isEmpty
    }

    var body: some View {
        VStack(spacing: appearance.stackSpacing) {
            VStack(spacing: 0) {
                TextFieldWrapper(
                    placeholder: Strings.Auth.Credentials.emailPlaceholder,
                    text: $emailText,
                    makeTextField: { AuthTextField(type: .text) },
                    configuration: .combined([.email, .partOfChain]),
                    onReturn: { passwordFirstResponderAction = .becomeFirstResponder }
                )
                .frame(height: appearance.textFieldHeight)

                Divider()
            }

            VStack(spacing: 0) {
                TextFieldWrapper(
                    placeholder: Strings.Auth.Credentials.passwordPlaceholder,
                    text: $passwordText,
                    makeTextField: { AuthTextField(type: .password) },
                    configuration: .combined([.password, .lastOfChainGo]),
                    firstResponderAction: $passwordFirstResponderAction,
                    onReturn: doLogIn
                )
                .frame(height: appearance.textFieldHeight)

                Divider()
            }

            if let errorMessage = errorMessage {
                AuthCredentialsErrorView(message: errorMessage)
            }

            Button(Strings.Auth.Credentials.logIn, action: doLogIn)
                .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
                .disabled(!isInputFulfilled)

            Button(
                Strings.Auth.Credentials.resetPassword,
                action: doResetPassword
            )
            .padding(appearance.resetPasswordButtonInset)
        }
        .padding()
        .background(appearance.backgroundColor)
        .addBorder(color: appearance.borderColor)
    }

    // MARK: Private API

    private func doLogIn() {
        endEditing()
        onLogIn?()
    }

    private func doResetPassword() {
        endEditing()
        onResetPassword?()
    }
}

#Preview("Light") {
    Group {
        AuthCredentialsFormView(
            emailText: .constant(""),
            passwordText: .constant("")
        )

        AuthCredentialsFormView(
            emailText: .constant(""),
            passwordText: .constant(""),
            errorMessage: "error"
        )
    }
    .padding(.horizontal)
}

#Preview("Dark") {
    Group {
        AuthCredentialsFormView(
            emailText: .constant(""),
            passwordText: .constant("")
        )

        AuthCredentialsFormView(
            emailText: .constant(""),
            passwordText: .constant(""),
            errorMessage: "error"
        )
    }
    .preferredColorScheme(.dark)
    .padding(.horizontal)
}
