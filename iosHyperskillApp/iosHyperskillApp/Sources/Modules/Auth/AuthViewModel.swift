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
        case .google:
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

                self.onNewMessage(AuthFeatureMessageAuthWithGoogle(accessToken: accessToken))
            }
        default:
            break
        }
    }
}
