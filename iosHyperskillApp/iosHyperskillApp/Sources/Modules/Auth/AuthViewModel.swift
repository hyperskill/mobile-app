import GoogleSignIn
import shared
import SwiftUI

final class AuthViewModel: FeatureViewModel<AuthFeatureState, AuthFeatureMessage, AuthFeatureActionViewAction> {
    func signInWithGoogle() {
        guard let currentRootViewController = UIApplication.shared.currentRootViewController else {
            return
        }

        if GIDSignIn.sharedInstance.hasPreviousSignIn() {
            GIDSignIn.sharedInstance.signOut()
        }

        GIDSignIn.sharedInstance.signIn(
            with: GIDConfiguration(
                clientID: GoogleServiceInfo.clientID,
                serverClientID: GoogleServiceInfo.serverClientID
            ),
            presenting: currentRootViewController
        ) { user, error in
            if let error = error {
                print("GIDSignIn :: error = \(error.localizedDescription)")
            } else if let serverAuthCode = user?.serverAuthCode {
                self.onNewMessage(AuthFeatureMessageAuthWithGoogle(accessToken: serverAuthCode))
            } else {
                print("GIDSignIn :: error missing serverAuthCode")
            }
        }
    }
}
