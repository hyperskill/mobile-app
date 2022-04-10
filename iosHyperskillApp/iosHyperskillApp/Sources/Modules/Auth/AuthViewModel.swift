import GoogleSignIn
import shared
import SwiftUI

enum SocialProvider: String, CaseIterable {
    case jetbrains
    case google
    case github
    case apple
}

final class AuthViewModel: FeatureViewModel<AuthFeatureState, AuthFeatureMessage, AuthFeatureActionViewAction> {
    let availableSocialAuthProviders = SocialProvider.allCases

    func signInWithSocialProvider(provider: SocialProvider) -> () -> Void {
        func inner() {
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
                    // todo pass accessToken to shared module
                    print(accessToken)
                }
            default:
                return
            }
        }

        return inner
    }
}
