import GoogleSignIn
import shared
import SwiftUI

enum SocialAuthProvider: String, CaseIterable {
    case jetbrains
    case google
    case github
    case apple
}

final class AuthSocialViewModel: FeatureViewModel<AuthFeatureState, AuthFeatureMessage, AuthFeatureActionViewAction> {
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
                    self.showAuthError(message: error.localizedDescription)
                } else if let serverAuthCode = user?.serverAuthCode {
                    self.onNewMessage(AuthFeatureMessageAuthWithGoogle(accessToken: serverAuthCode))
                } else {
                    self.showAuthError(message: "error missing serverAuthCode")
                }
            }
        case .github:
            break
        case .apple:
            break
        }
    }

    private func showAuthError(message: String) {
        let viewAction = AuthFeatureActionViewActionShowAuthError(errorMsg: message)
        self.onViewAction?(viewAction)
    }
}
