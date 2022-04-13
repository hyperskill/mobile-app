import SwiftUI

struct AuthEmailFormView: View {
    @State private var email: String = ""
    @State private var password: String = ""

    var body: some View {
        VStack(alignment: .center, spacing: 16) {
            TextField(Strings.emailPlaceHolderText, text: $email).font(.body)
            Divider()

            SecureField(Strings.passwordPlaceHolderText, text: $password).font(.body)
            Divider()

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
            ).cornerRadius(8)

            Button(
                action: {},
                label: {
                    Text(Strings.resetPasswordText)
                        .font(.body)
                        .foregroundColor(Color(ColorPalette.primary))
                }
            ).padding(.vertical, 8)
        }
        .padding()
        .background(Color.white)
        .cornerRadius(8)
    }
}

struct AuthEmailFormView_Previews: PreviewProvider {
    static var previews: some View {
        AuthEmailFormView()
    }
}
