import SwiftUI

struct AuthSocialControlsView: View {
    let isInSignUpMode: Bool

    let errorMessage: String?

    let onSocialAuthProviderClick: ((SocialAuthProvider) -> Void)
    let onContinueWithEmailClick: (() -> Void)

    @State private var isShowingMoreOptions = false

    private var socialAuthProviders: [SocialAuthProvider] {
        let providers: [SocialAuthProvider] =
            if isInSignUpMode {
                isShowingMoreOptions ? [.google, .apple, .jetbrains, .github] : [.google, .apple]
            } else {
                [.jetbrains, .google, .github, .apple]
            }
        return providers.filter(\.isSupported)
    }

    var body: some View {
        VStack(spacing: LayoutInsets.smallInset) {
            ForEach(socialAuthProviders, id: \.rawValue) { provider in
                AuthSocialButton(
                    text: provider.humanReadableName,
                    imageName: provider.imageName,
                    action: {
                        onSocialAuthProviderClick(provider)
                    }
                )
            }

            if let errorMessage {
                AuthCredentialsErrorView(message: errorMessage)
            }

            if isInSignUpMode {
                if !isShowingMoreOptions {
                    Button(
                        Strings.Auth.Social.moreOptionsButton,
                        action: {
                            withAnimation {
                                isShowingMoreOptions = true
                            }
                        }
                    )
                    .padding(.top)
                }
            } else {
                Button(
                    Strings.Auth.Social.emailText,
                    action: onContinueWithEmailClick
                )
                .padding(.top)
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
                isInSignUpMode: false,
                errorMessage: "Error message",
                onSocialAuthProviderClick: { _ in },
                onContinueWithEmailClick: {}
            )
            .preferredColorScheme(.light)

            AuthSocialControlsView(
                isInSignUpMode: false,
                errorMessage: "Error message",
                onSocialAuthProviderClick: { _ in },
                onContinueWithEmailClick: {}
            )
            .preferredColorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
