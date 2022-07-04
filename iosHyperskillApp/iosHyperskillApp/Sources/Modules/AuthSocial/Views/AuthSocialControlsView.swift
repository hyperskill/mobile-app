import SwiftUI

extension AuthSocialControlsView {
    struct Appearance {
        let continueWithEmailButtonLayoutInsets = LayoutInsets(top: 24)
    }
}

struct AuthSocialControlsView: View {
    private(set) var appearance = Appearance()

    let socialAuthProviders: [SocialAuthProvider]

    private let onSocialAuthProviderClick: ((SocialAuthProvider) -> Void)

    private let onContinueWithEmailClick: (() -> Void)

    init(
        socialAuthProviders: [SocialAuthProvider],
        onSocialAuthProviderClick: @escaping (SocialAuthProvider) -> Void = { _ in },
        onContinueWithEmailClick: @escaping () -> Void = {}
    ) {
        self.socialAuthProviders = socialAuthProviders
        self.onSocialAuthProviderClick = onSocialAuthProviderClick
        self.onContinueWithEmailClick = onContinueWithEmailClick
    }

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

            Button(Strings.Auth.Social.emailText, action: self.onContinueWithEmailClick)
                .padding(appearance.continueWithEmailButtonLayoutInsets.edgeInsets)
                .buttonStyle(OutlineButtonStyle(style: .violet))
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
            AuthSocialControlsView(socialAuthProviders: SocialAuthProvider.allCases)
                .preferredColorScheme(.light)

            AuthSocialControlsView(socialAuthProviders: SocialAuthProvider.allCases)
                .preferredColorScheme(.dark)
        }
    }
}
