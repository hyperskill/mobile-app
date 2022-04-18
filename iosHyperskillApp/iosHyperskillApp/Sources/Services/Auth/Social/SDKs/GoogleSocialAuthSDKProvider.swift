import GoogleSignIn
import UIKit

@MainActor
final class GoogleSocialAuthSDKProvider: SocialAuthSDKProvider {
    static let shared = GoogleSocialAuthSDKProvider()

    private init() {}

    func signIn() async throws -> SocialAuthSDKResponse {
        try await withCheckedThrowingContinuation { continuation in
            self.signIn { result in
                switch result {
                case .success(let response):
                    continuation.resume(returning: response)
                case .failure(let error):
                    continuation.resume(throwing: error)
                }
            }
        }
    }

    private func signIn(completion: @escaping (Result<SocialAuthSDKResponse, SocialAuthSDKError>) -> Void) {
        guard let currentRootViewController = UIApplication.shared.currentRootViewController else {
            return completion(.failure(.noPresentingViewController))
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
                print("GoogleSocialAuthSDKProvider :: error = \(error.localizedDescription)")
                if (error as NSError).code == GIDSignInError.canceled.rawValue {
                    completion(.failure(.canceled))
                } else {
                    completion(.failure(.connectionError))
                }
            } else if let serverAuthCode = user?.serverAuthCode {
                print("GoogleSocialAuthSDKProvider :: success serverAuthCode = \(serverAuthCode)")
                let token = SocialAuthSDKResponse(token: serverAuthCode)
                completion(.success(token))
            } else {
                print("GoogleSocialAuthSDKProvider :: error missing serverAuthCode")
                completion(.failure(.accessDenied))
            }
        }
    }
}
