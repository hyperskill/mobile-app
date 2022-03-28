import Foundation
import GoogleSignIn

final class AuthViewModel {
    func signInWithGoogle() {
        guard let currentRootViewController = UIApplication.shared.currentRootViewController else {
            return
        }
        
        GIDSignIn.sharedInstance.signIn(
            with: GIDConfiguration(clientID: GoogleServiceInfo.clientID),
            presenting: currentRootViewController
        ) { user, error in
            guard error == nil else {
                return
            }
            guard let accessToken = user?.authentication.accessToken else {
                return
            }
            
            // todo pass accessToken to shared module
            print(accessToken)
        }
    }
}
