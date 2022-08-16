import AuthenticationServices
import Foundation

@MainActor
final class AppleIDSocialAuthSDKProvider: NSObject, SocialAuthSDKProvider {
    private typealias CompletionHandler = (Result<SocialAuthSDKResponse, SocialAuthSDKError>) -> Void

    static let shared = AppleIDSocialAuthSDKProvider()

    private var completionHandler: CompletionHandler?

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

    private func signIn(completionHandler: @escaping CompletionHandler) {
        self.completionHandler = completionHandler

        let appleIDProvider = ASAuthorizationAppleIDProvider()
        let request = appleIDProvider.createRequest()
        request.requestedScopes = [.fullName, .email]

        let authorizationController = ASAuthorizationController(authorizationRequests: [request])
        authorizationController.delegate = self
        authorizationController.presentationContextProvider = self
        authorizationController.performRequests()
    }
}

extension AppleIDSocialAuthSDKProvider: ASAuthorizationControllerDelegate {
    func authorizationController(
        controller: ASAuthorizationController,
        didCompleteWithAuthorization authorization: ASAuthorization
    ) {
        guard let appleIDCredential = authorization.credential as? ASAuthorizationAppleIDCredential else {
            return handleDidCompleteWithError(SocialAuthSDKError.connectionError)
        }

        guard let appleAuthCode = appleIDCredential.authorizationCode else {
            print("AppleIDSocialAuthSDKProvider :: Unable to fetch authorization code")
            return handleDidCompleteWithError(SocialAuthSDKError.connectionError)
        }

        guard let authCodeString = String(data: appleAuthCode, encoding: .utf8) else {
            print("AppleIDSocialAuthSDKProvider :: Unable to serialize authorization code from data: \(appleAuthCode.debugDescription)")
            return handleDidCompleteWithError(SocialAuthSDKError.connectionError)
        }

        guard let appleIDToken = appleIDCredential.identityToken else {
            print("AppleIDSocialAuthSDKProvider :: Unable to fetch identity token")
            return handleDidCompleteWithError(SocialAuthSDKError.connectionError)
        }

        guard let idTokenString = String(data: appleIDToken, encoding: .utf8) else {
            print("AppleIDSocialAuthSDKProvider :: Unable to serialize token string from data: \(appleIDToken.debugDescription)")
            return handleDidCompleteWithError(SocialAuthSDKError.connectionError)
        }

        completionHandler?(.success(SocialAuthSDKResponse(token: idTokenString)))
    }

    func authorizationController(controller: ASAuthorizationController, didCompleteWithError error: Error) {
        handleDidCompleteWithError(error)
    }

    private func handleDidCompleteWithError(_ error: Error) {
        let sdkError: SocialAuthSDKError = {
            if let sdkError = error as? SocialAuthSDKError {
                return sdkError
            } else if let authorizationError = error as? ASAuthorizationError {
                switch authorizationError.code {
                case .canceled:
                    return .accessDenied
                case .unknown, .invalidResponse, .notHandled, .failed, .notInteractive:
                    return .connectionError
                @unknown default:
                    return .connectionError
                }
            } else {
                return .connectionError
            }
        }()

        completionHandler?(.failure(sdkError))
    }
}

extension AppleIDSocialAuthSDKProvider: ASAuthorizationControllerPresentationContextProviding {
    func presentationAnchor(for controller: ASAuthorizationController) -> ASPresentationAnchor {
        SourcelessRouter().window.require()
    }
}
