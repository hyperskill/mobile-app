import SwiftUI

extension AuthNewUserPlaceholderView {
    struct Appearance {
        let logoWidthHeight: CGFloat = 48
        let bigPadding: CGFloat = 48
        let smallPadding: CGFloat = 20
    }
}

struct AuthNewUserPlaceholderView: View {
    private static let registerURL = HyperskillURLFactory.makeRegister()

    private(set) var appearance = Appearance()

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.bigPadding) {
            VStack(alignment: .center, spacing: appearance.smallPadding) {
                Image(Images.AuthSocial.hyperskill)
                    .resizable()
                    .frame(widthHeight: appearance.logoWidthHeight)

                Text(Strings.Auth.NewUserPlaceholder.title)
                    .font(.title2)
                    .foregroundColor(.primaryText)
            }
            .frame(maxWidth: .infinity)


            Text(Strings.Auth.NewUserPlaceholder.introText)
                .font(.body)
                .foregroundColor(.primaryText)

            if let registerURL = Self.registerURL {
                URLButton(text: Strings.Auth.NewUserPlaceholder.buttonText, url: registerURL)
                    .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
            }


            VStack(alignment: .leading, spacing: appearance.smallPadding) {
                Text(Strings.Auth.NewUserPlaceholder.possibilityText)
                Text(Strings.Auth.NewUserPlaceholder.callText)
            }
            .font(.body)
            .foregroundColor(.primaryText)

            Spacer()
        }
        .padding()
    }
}

struct AuthNewUserPlaceholderView_Previews: PreviewProvider {
    static var previews: some View {
        AuthNewUserPlaceholderView()
    }
}
