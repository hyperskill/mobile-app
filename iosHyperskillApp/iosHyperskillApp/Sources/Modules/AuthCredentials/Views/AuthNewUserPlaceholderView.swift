import SwiftUI

extension AuthNewUserPlaceholderView {
    struct Appearance {
        let logoWidthHeight: CGFloat = 48

        let spacingLarge: CGFloat = 48
        let spacingSmall: CGFloat = 20

        let contentMaxWidth: CGFloat = 400
    }
}

struct AuthNewUserPlaceholderView: View {
    private static let registerURL = HyperskillURLFactory.makeRegister()

    private(set) var appearance = Appearance()

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    let onSignInTap: () -> Void

    var body: some View {
        ZStack {
            BackgroundView()

            VStack(spacing: 0) {
                if horizontalSizeClass == .regular {
                    Spacer()
                }

                content

                Spacer()
            }
            .frame(maxWidth: appearance.contentMaxWidth)
            .padding()
        }
    }

    private var content: some View {
        VStack(alignment: .leading, spacing: appearance.spacingLarge) {
            VStack(alignment: .center, spacing: appearance.spacingSmall) {
                HyperskillLogoView(logoWidthHeight: appearance.logoWidthHeight)

                Text(Strings.Auth.NewUserPlaceholder.title)
                    .font(.title2)
                    .foregroundColor(.primaryText)
            }
            .frame(maxWidth: .infinity)

            Text(Strings.Auth.NewUserPlaceholder.introText)
                .font(.body)
                .foregroundColor(.primaryText)

            VStack(alignment: .center, spacing: appearance.spacingSmall) {
                OpenURLInsideAppButton(
                    text: Strings.Auth.NewUserPlaceholder.buttonText, url: Self.registerURL.require()
                )
                .buttonStyle(RoundedRectangleButtonStyle(style: .violet))

                Button(Strings.Auth.Credentials.logIn) {
                    onSignInTap()
                }
                .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
            }

            Text(Strings.Auth.NewUserPlaceholder.possibilityText)
                .font(.body)
                .foregroundColor(.primaryText)
        }
    }
}

struct AuthNewUserPlaceholderView_Previews: PreviewProvider {
    static var previews: some View {
        AuthNewUserPlaceholderView(onSignInTap: {})

        AuthNewUserPlaceholderView(onSignInTap: {})
            .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
    }
}
