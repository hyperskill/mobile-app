import shared
import SwiftUI

final class AuthSocialViewModel: FeatureViewModel<
  AuthSocialFeatureState,
  AuthSocialFeatureMessage,
  AuthSocialFeatureActionViewAction
> {
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

                let message = AuthSocialFeatureMessageAuthWithSocial(
                    authCode: authCode,
                    socialAuthProvider: provider.sharedType
                )

                self.onNewMessage(message)
            } catch {
                print("AuthSocialViewModel :: signIn error = \(error)")
                //await self.showAuthError(message: error.localizedDescription)
            }
        }
    }

//    @MainActor
//    private func showAuthError(message: String) {
//        let viewAction = AuthSocialFeatureActionViewActionShowAuthError(errorMessage: message)
//        self.onViewAction?(viewAction)
//    }
}
