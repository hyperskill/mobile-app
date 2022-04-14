import SwiftUI

extension AuthEmailFormView {
    struct Appearance {
        let textFieldHeight: CGFloat = 44

        let spacing: CGFloat = 16

        let cornerRadius: CGFloat = 8
    }
}

struct AuthEmailFormView: View {
    private(set) var appearance = Appearance()

    @State private var emailText = ""

    @State private var passwordText = ""
    @State private var passwordFirstResponderAction: TextFieldWrapper.FirstResponderAction?

    @State private var error = false

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
                Text(Strings.emailLoginErrorText)
                    .foregroundColor(Color(ColorPalette.error))
                    .font(.caption)
                    .padding()
                    .background(Color(ColorPalette.error).opacity(0.12))
                    .cornerRadius(appearance.cornerRadius)
            }

            // todo видел уже такая кнопка реализована в ui степа, надо бы ее переиспользовать тут
            Button(
                action: {},
                label: {
                    Text(Strings.logInText)
                        .font(.body)
                        .frame(maxWidth: .infinity, minHeight: 44, alignment: .center)
                        .foregroundColor(.white)
                        .background(Color(ColorPalette.primary))
                }
            )
            .cornerRadius(appearance.cornerRadius)
            .disabled(emailText.isEmpty || passwordText.isEmpty)

            Button(
                action: {},
                label: {
                    Text(Strings.resetPasswordText)
                        .font(.body)
                        .foregroundColor(Color(ColorPalette.primary))
                }
            )
        }
        .padding()
        .background(Color.white)
        .cornerRadius(appearance.cornerRadius)
    }
}

struct AuthEmailFormView_Previews: PreviewProvider {
    static var previews: some View {
        AuthEmailFormView()
    }
}
