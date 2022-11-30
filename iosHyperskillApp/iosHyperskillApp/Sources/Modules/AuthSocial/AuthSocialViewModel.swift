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

    override func shouldNotifyStateDidChange(
        oldState: AuthSocialFeatureState,
        newState: AuthSocialFeatureState
    ) -> Bool {
        AuthSocialFeatureStateKs(oldState) != AuthSocialFeatureStateKs(newState)
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
                    let originalError: KotlinThrowable = {
                        let defaultError = KotlinThrowable(message: String(describing: error))

                        guard let sdkError = error as? SocialAuthSDKError else {
                            return defaultError
                        }

                        switch sdkError {
                        case .canceled, .noPresentingViewController:
                            return defaultError
                        case .accessDenied(let originalError):
                            return KotlinThrowable(message: String(describing: originalError))
                        case .connectionError(let originalError):
                            return KotlinThrowable(message: String(describing: originalError))
                        }
                    }()

                    let message = AuthSocialFeatureMessageSocialAuthProviderAuthFailureEventMessage(
                        data: AuthSocialFeatureMessageAuthFailureData(
                            socialAuthProvider: provider.sharedType,
                            socialAuthError: .connectionProblem,
                            originalError: originalError
                        )
                    )

                    self.onNewMessage(message)
                }
            }
        }
    }

    func getAuthSocialErrorText(authSocialError: AuthSocialError) -> String {
        self.authSocialErrorMapper.getAuthSocialErrorText(authSocialError: authSocialError)
    }

    func doCompleteAuthFlow(profile: Profile) {
        moduleOutput?.handleUserAuthorized(profile: profile)
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
