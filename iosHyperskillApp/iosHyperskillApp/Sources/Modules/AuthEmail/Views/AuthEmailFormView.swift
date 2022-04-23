import SwiftUI

extension AuthEmailFormView {
    struct Appearance {
        let stackSpacing = LayoutInsets.defaultInset

        let textFieldHeight: CGFloat = 44

        let resetPasswordButtonInset = LayoutInsets.smallInset

        let backgroundColor = UIColor.dynamic(light: ColorPalette.surface, dark: .secondarySystemBackground)
    }
}

struct AuthEmailFormView: View {
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
                    placeholder: Strings.authEmailPlaceholder,
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
                    placeholder: Strings.authEmailPasswordPlaceholder,
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
                AuthEmailErrorView(message: errorMessage)
            }

            Button(Strings.authEmailLogIn, action: doLogIn)
                .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
                .disabled(!isInputFulfilled || errorMessage != nil)

            Button(
                action: doResetPassword,
                label: {
                    Text(Strings.authEmailResetPassword)
                        .font(.body)
                        .foregroundColor(Color(ColorPalette.primary))
                }
            )
            .padding(appearance.resetPasswordButtonInset)
        }
        .padding()
        .background(Color(appearance.backgroundColor))
        .addBorder()
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

struct AuthEmailFormView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            Group {
                AuthEmailFormView(
                    emailText: .constant(""),
                    passwordText: .constant("")
                )

                AuthEmailFormView(
                    emailText: .constant(""),
                    passwordText: .constant(""),
                    errorMessage: "error"
                )
            }

            Group {
                AuthEmailFormView(
                    emailText: .constant(""),
                    passwordText: .constant("")
                )

                AuthEmailFormView(
                    emailText: .constant(""),
                    passwordText: .constant(""),
                    errorMessage: "error"
                )
            }
            .preferredColorScheme(.dark)
        }
        .padding(.horizontal)
    }
}
