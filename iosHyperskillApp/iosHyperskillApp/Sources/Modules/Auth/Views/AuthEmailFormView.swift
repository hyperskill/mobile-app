import SwiftUI

extension AuthEmailFormView {
    struct Appearance {
        let textFieldHeight: CGFloat = 44

        let spacing: CGFloat = 16

        let cornerRadius: CGFloat = 8

        let backgroundColor = UIColor.dynamic(light: ColorPalette.surface, dark: .secondarySystemBackground)
    }
}

struct AuthEmailFormView: View {
    private(set) var appearance = Appearance()

    @State private var emailText = ""

    @State private var passwordText = ""
    @State private var passwordFirstResponderAction: TextFieldWrapper.FirstResponderAction?

    @State private(set) var error = false

    var body: some View {
        VStack(spacing: appearance.spacing) {
            VStack(spacing: 0) {
                TextFieldWrapper(
                    placeholder: Strings.emailPlaceHolderText,
                    text: $emailText,
                    configuration: .combined([.email, .partOfChain]),
                    onReturn: { passwordFirstResponderAction = .becomeFirstResponder }
                )
                .frame(height: appearance.textFieldHeight)

                Divider()
            }

            VStack(spacing: 0) {
                TextFieldWrapper(
                    placeholder: Strings.passwordPlaceHolderText,
                    text: $passwordText,
                    configuration: .combined([.password, .lastOfChainGo]),
                    firstResponderAction: $passwordFirstResponderAction,
                    onReturn: { passwordFirstResponderAction = .resignFirstResponder }
                )
                .frame(height: appearance.textFieldHeight)

                Divider()
            }

            if error {
                AuthEmailErrorView()
            }

            Button(Strings.logInText, action: {})
                .buttonStyle(RoundedRectangleButtonStyle(minHeight: 44, backgroundColor: Color(ColorPalette.primary)))
                .disabled(emailText.isEmpty || passwordText.isEmpty)

            Button(
                action: {},
                label: {
                    Text(Strings.resetPasswordText)
                        .font(.body)
                        .foregroundColor(Color(ColorPalette.primary))
                }
            )
            .padding(8)
        }
        .padding()
        .background(Color(appearance.backgroundColor))
        .addBorder()
    }
}

struct AuthEmailFormView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            Group {
                AuthEmailFormView(error: false)

                AuthEmailFormView(error: true)
            }

            Group {
                AuthEmailFormView(error: false)

                AuthEmailFormView(error: true)
            }
            .preferredColorScheme(.dark)
        }
        .padding(.horizontal)
    }
}
