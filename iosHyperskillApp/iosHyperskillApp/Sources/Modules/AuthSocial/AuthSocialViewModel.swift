import GoogleSignIn
import shared
import SwiftUI

final class AuthSocialViewModel: FeatureViewModel<AuthFeatureState, AuthFeatureMessage, AuthFeatureActionViewAction> {
    private let socialAuthService: SocialAuthServiceProtocol

    let availableSocialAuthProviders = SocialAuthProvider.allCases.filter(\.isSupported)

    init(socialAuthService: SocialAuthServiceProtocol, feature: Presentation_reduxFeature) {
        self.socialAuthService = socialAuthService
        super.init(feature: feature)
    }

    func signIn(with provider: SocialAuthProvider) {
        Task {
            do {
                let response = try await self.socialAuthService.signIn(with: provider)

                if let authCode = response.authCode {
                    self.onNewMessage(AuthFeatureMessageAuthWithCode(authCode: authCode))
                } else if let socialToken = response.socialToken {
                    self.onNewMessage(
                        AuthFeatureMessageAuthWithSocialToken(authCode: socialToken, provider: provider.sharedType)
                    )
                }
            } catch {
                print("AuthSocialViewModel :: signIn error = \(error)")
            }
        }
    }
}
