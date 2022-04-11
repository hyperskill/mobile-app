import GoogleSignIn
import shared
import SwiftUI

enum SocialAuthProvider: String, CaseIterable {
    case jetbrains
    case google
    case github
    case apple
}

final class AuthViewModel: FeatureViewModel<AuthFeatureState, AuthFeatureMessage, AuthFeatureActionViewAction> {
    let availableSocialAuthProviders = SocialAuthProvider.allCases

    func signInWithSocialAuthProvider(_ provider: SocialAuthProvider) {
        switch provider {
        case .jetbrains:
            break
        case .google:
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
        case .github:
            break
        case .apple:
            break
        }
    }
}
