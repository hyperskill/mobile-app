import Foundation
import GoogleSignIn

final class AuthViewModel {
    func googleSignIn() {
        GIDSignIn.sharedInstance.signIn(
            with: GIDConfiguration(clientID: GoogleServiceInfo.clientID),
            presenting: UIApplication.shared.currentRootViewController!
        ) { user, error in
            guard error == nil else { return }
            guard user != nil else { return }
            // todo pass accessToken to shared module
            print(user!.authentication.accessToken)
        }
    }
}
