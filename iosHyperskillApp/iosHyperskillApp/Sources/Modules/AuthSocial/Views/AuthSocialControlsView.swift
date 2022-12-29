import SwiftUI

extension AuthSocialControlsView {
    struct Appearance {
        let continueWithEmailButtonFont = Font.body
        let continueWithEmailButtonTextColor = ColorPalette.primary
        let continueWithEmailButtonLayoutInsets = LayoutInsets(top: 24)
    }
}

struct AuthSocialControlsView: View {
    private(set) var appearance = Appearance()

    let socialAuthProviders: [SocialAuthProvider]

    let isContinueWithEmailAvailable: Bool

    let onSocialAuthProviderClick: ((SocialAuthProvider) -> Void)
    let onContinueWithEmailClick: (() -> Void)

    var body: some View {
        VStack {
            ForEach(socialAuthProviders, id: \.rawValue) { provider in
                AuthSocialButton(
                    text: provider.humanReadableName,
                    imageName: provider.imageName,
                    action: {
                        self.onSocialAuthProviderClick(provider)
                    }
                )
            }

            if isContinueWithEmailAvailable {
                Button(Strings.Auth.Social.emailText, action: self.onContinueWithEmailClick)
                    .font(.body)
                    .foregroundColor(Color(appearance.continueWithEmailButtonTextColor))
                    .padding(appearance.continueWithEmailButtonLayoutInsets.edgeInsets)
            }
        }
    }
}

private extension SocialAuthProvider {
    var humanReadableName: String {
        switch self {
        case .jetbrains:
            return Strings.Auth.Social.jetBrainsAccount
        case .google:
            return Strings.Auth.Social.googleAccount
        case .github:
            return Strings.Auth.Social.gitHubAccount
        case .apple:
            return Strings.Auth.Social.appleAccount
        }
    }

    var imageName: String {
        switch self {
        case .jetbrains:
            return Images.AuthSocial.jetbrains
        case .google:
            return Images.AuthSocial.google
        case .github:
            return Images.AuthSocial.github
        case .apple:
            return Images.AuthSocial.apple
        }
    }
}

struct AuthSocialControlsView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            AuthSocialControlsView(
                socialAuthProviders: SocialAuthProvider.allCases,
                isContinueWithEmailAvailable: true,
                onSocialAuthProviderClick: { _ in },
                onContinueWithEmailClick: {}
            )
            .preferredColorScheme(.light)

            AuthSocialControlsView(
                socialAuthProviders: SocialAuthProvider.allCases,
                isContinueWithEmailAvailable: true,
                onSocialAuthProviderClick: { _ in },
                onContinueWithEmailClick: {}
            )
            .preferredColorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
