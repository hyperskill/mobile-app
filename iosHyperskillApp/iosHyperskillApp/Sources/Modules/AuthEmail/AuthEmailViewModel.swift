import Foundation
import shared

final class AuthEmailViewModel: FeatureViewModel<
  AuthCredentialsFeatureState,
  AuthCredentialsFeatureMessage,
  AuthCredentialsFeatureActionViewAction
> {
    func doFormInputChange(email: String, password: String) {
        let message = AuthCredentialsFeatureMessageAuthEditing(email: email, password: password)
        self.onNewMessage(message)
    }

    func doLogIn() {
        self.onNewMessage(AuthCredentialsFeatureMessageAuthWithEmail())
    }

    func doResetPassword() {
        print("AuthEmailViewModel :: \(#function)")
    }
}
