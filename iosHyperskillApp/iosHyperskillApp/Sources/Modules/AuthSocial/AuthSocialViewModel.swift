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

            ProgressHUD.show()

            GIDSignIn.sharedInstance.signIn(
                with: GIDConfiguration(
                    clientID: GoogleServiceInfo.clientID,
                    serverClientID: GoogleServiceInfo.serverClientID
                ),
                presenting: currentRootViewController
            ) { user, error in
                if let error = error {
                    ProgressHUD.showError(status: error.localizedDescription)
                } else if let serverAuthCode = user?.serverAuthCode {
                    self.onNewMessage(AuthFeatureMessageAuthWithGoogle(accessToken: serverAuthCode))
                    ProgressHUD.dismiss()
                } else {
                    // todo я не знаю, какое именно тут сообщение писать, нужно что-то более осмысленное
                    ProgressHUD.showError(status: "GIDSignIn :: error missing serverAuthCode")
                }
            }
        case .github:
            break
        case .apple:
            break
        }
    }
}
