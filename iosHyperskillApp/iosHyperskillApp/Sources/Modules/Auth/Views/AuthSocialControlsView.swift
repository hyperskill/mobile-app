import SwiftUI

struct AuthSocialControlsView: View {
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

            Button(Strings.authEmailText, action: self.onContinueWithEmailClick)
                .padding(.top)
                .buttonStyle(OutlineButtonStyle(style: .violet))
        }
        .padding(.horizontal)
    }
}

private extension SocialAuthProvider {
    var humanReadableName: String {
        switch self {
        case .jetbrains:
            return Strings.authJetBrainsAccountText
        case .google:
            return Strings.authGoogleAccountText
        case .github:
            return Strings.authGitHubAccountText
        case .apple:
            return Strings.authAppleAccountText
        }
    }

    var imageName: String {
        switch self {
        case .jetbrains:
            return "jetbrains_logo"
        case .google:
            return "google_logo"
        case .github:
            return "github_logo"
        case .apple:
            return "apple_logo"
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
