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

        return self.authCredentialsErrorMapper.getAuthCredentialsErrorText(authCredentialsError: authCredentialsError)
    }

    init(authCredentialsErrorMapper: AuthCredentialsErrorMapper, feature: Presentation_reduxFeature) {
        self.authCredentialsErrorMapper = authCredentialsErrorMapper
        super.init(feature: feature)
    }

    func doFormInputChange(email: String, password: String) {
        let message = AuthCredentialsFeatureMessageAuthEditing(email: email, password: password)
        self.onNewMessage(message)
    }

    func doLogIn() {
        self.onNewMessage(AuthCredentialsFeatureMessageSubmitFormClicked())
    }

    func doResetPassword() {
        print("AuthEmailViewModel :: \(#function)")
    }

    func doCompleteAuthFlow(isNewUser: Bool) {
        moduleOutput?.handleUserAuthorized(isNewUser: isNewUser)
    }
}
