import Foundation
import shared

final class AuthEmailViewModel: FeatureViewModel<
  AuthCredentialsFeatureState,
  AuthCredentialsFeatureMessage,
  AuthCredentialsFeatureActionViewAction
> {
    func logIn(email: String, password: String) {
        let message = AuthCredentialsFeatureMessageAuthWithEmail(email: email, password: password)
        self.onNewMessage(message)
    }

    func resetPassword() {
        print("AuthEmailViewModel :: \(#function)")
    }
}
