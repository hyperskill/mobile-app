import shared
import SwiftUI

final class AuthSocialViewModel: FeatureViewModel<
  AuthSocialFeatureState,
  AuthSocialFeatureMessage,
  AuthSocialFeatureActionViewAction
> {
    weak var moduleOutput: AuthOutputProtocol?

    private let socialAuthService: SocialAuthServiceProtocol

    private let authSocialErrorMapper: AuthSocialErrorMapper

    let availableSocialAuthProviders = SocialAuthProvider.allCases.filter(\.isSupported)

    init(
        socialAuthService: SocialAuthServiceProtocol,
        authSocialErrorMapper: AuthSocialErrorMapper,
        feature: Presentation_reduxFeature
    ) {
        self.socialAuthService = socialAuthService
        self.authSocialErrorMapper = authSocialErrorMapper
        super.init(feature: feature)
    }

    func signIn(with provider: SocialAuthProvider) {
        logClickedSignInWithSocialEvent(provider: provider)

        Task(priority: .userInitiated) {
            do {
                let response = try await self.socialAuthService.signIn(with: provider)

                await MainActor.run {
                    let message = AuthSocialFeatureMessageAuthWithSocial(
                        authCode: response.authorizationCode,
                        idToken: response.identityToken,
                        socialAuthProvider: provider.sharedType
                    )

                    self.onNewMessage(message)
                }
            } catch {
                #if DEBUG
                print("AuthSocialViewModel :: signIn error = \(error)")
                #endif

                if case SocialAuthError.canceled = error {
                    return
                }

                await MainActor.run {
                    self.onViewAction?(AuthSocialFeatureActionViewActionShowAuthError(socialError: .connectionProblem))
                }
            }
        }
    }

    func getAuthSocialErrorText(authSocialError: AuthSocialError) -> String {
        self.authSocialErrorMapper.getAuthSocialErrorText(authSocialError: authSocialError)
    }

    func doCompleteAuthFlow(isNewUser: Bool) {
        moduleOutput?.handleUserAuthorized(isNewUser: isNewUser)
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(AuthSocialFeatureMessageViewedEventMessage())
    }

    private func logClickedSignInWithSocialEvent(provider: SocialAuthProvider) {
        onNewMessage(
            AuthSocialFeatureMessageClickedSignInWithSocialEventMessage(socialAuthProvider: provider.sharedType)
        )
    }

    func logClickedContinueWithEmailEvent() {
        onNewMessage(AuthSocialFeatureMessageClickedContinueWithEmailEventMessage())
    }
}
