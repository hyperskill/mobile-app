import GoogleSignIn
import UIKit

enum GoogleSocialAuthSDKProviderErrorReason: Error {
    case noServerAuthCode
}

@MainActor
final class GoogleSocialAuthSDKProvider: SocialAuthSDKProvider {
    static let shared = GoogleSocialAuthSDKProvider()

    private let sourcelessRouter: SourcelessRouter

    private init() {
        self.sourcelessRouter = SourcelessRouter()
    }

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
        guard let currentPresentedViewController = self.sourcelessRouter.currentPresentedViewController() else {
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
            presenting: currentPresentedViewController
        ) { user, error in
            if let error = error {
                #if DEBUG
                print("GoogleSocialAuthSDKProvider :: error = \(error.localizedDescription)")
                #endif
                if (error as NSError).code == GIDSignInError.canceled.rawValue {
                    completion(.failure(.canceled))
                } else {
                    completion(.failure(.connectionError(originalError: error)))
                }
            } else if let serverAuthCode = user?.serverAuthCode {
                #if DEBUG
                print("GoogleSocialAuthSDKProvider :: success serverAuthCode = \(serverAuthCode)")
                #endif
                completion(.success(SocialAuthSDKResponse(authorizationCode: serverAuthCode)))
            } else {
                print("GoogleSocialAuthSDKProvider :: error missing serverAuthCode")
                completion(
                    .failure(.accessDenied(originalError: GoogleSocialAuthSDKProviderErrorReason.noServerAuthCode))
                )
            }
        }
    }
}
