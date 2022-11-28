import Foundation
import shared

final class AuthCredentialsViewModel: FeatureViewModel<
  AuthCredentialsFeatureState,
  AuthCredentialsFeatureMessage,
  AuthCredentialsFeatureActionViewAction
> {
    weak var moduleOutput: AuthOutputProtocol?

    private let authCredentialsErrorMapper: AuthCredentialsErrorMapper

    var formErrorMessage: String? {
        guard let errorState = self.state.formState as? AuthCredentialsFeatureFormStateError else {
            return nil
        }

        let authCredentialsError = errorState.credentialsError

        return authCredentialsErrorMapper.getAuthCredentialsErrorText(authCredentialsError: authCredentialsError)
    }

    init(authCredentialsErrorMapper: AuthCredentialsErrorMapper, feature: Presentation_reduxFeature) {
        self.authCredentialsErrorMapper = authCredentialsErrorMapper
        super.init(feature: feature)
    }

    func doFormInputChange(email: String, password: String) {
        onNewMessage(AuthCredentialsFeatureMessageAuthEditing(email: email, password: password))
    }

    func doLogIn() {
        logClickedSignInEvent()
        onNewMessage(AuthCredentialsFeatureMessageSubmitFormClicked())
    }

    func doResetPassword() {
        onNewMessage(AuthCredentialsFeatureMessageClickedResetPassword())
    }

    func doCompleteAuthFlow(isNewUser: Bool) {
        moduleOutput?.handleUserAuthorized(isNewUser: isNewUser)
    }

    func logAuthErrorToSentry(_ error: KotlinThrowable) {
        SentryManager.captureErrorMessage("AuthCredentials: \(String(describing: error))")
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(AuthCredentialsFeatureMessageViewedEventMessage())
    }

    private func logClickedSignInEvent() {
        onNewMessage(AuthCredentialsFeatureMessageClickedSignInEventMessage())
    }

    func logClickedContinueWithSocialEvent() {
        onNewMessage(AuthCredentialsFeatureMessageClickedContinueWithSocialEventMessage())
    }
}
