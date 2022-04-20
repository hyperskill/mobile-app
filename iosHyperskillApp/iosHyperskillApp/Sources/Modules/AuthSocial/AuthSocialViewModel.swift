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

                guard let authCode = response.authCode ?? response.socialToken else {
                    throw SocialAuthError.accessDenied
                }

                let message = AuthFeatureMessageAuthWithSocial(
                    authCode: authCode,
                    socialProvider: provider.sharedType
                )

                self.onNewMessage(message)
            } catch {
                print("AuthSocialViewModel :: signIn error = \(error)")
            }
        }
    }
}
