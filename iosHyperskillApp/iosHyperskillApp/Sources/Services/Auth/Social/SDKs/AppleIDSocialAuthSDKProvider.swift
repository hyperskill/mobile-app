import AuthenticationServices
import Foundation

enum AppleIDSocialAuthSDKProviderErrorReason: Error {
    case noASAuthorizationAppleIDCredential
    case noAuthorizationCode
    case failedSerializeAuthorizationCode
    case noIdentityToken
    case failedSerializeIdentityToken
}

@MainActor
final class AppleIDSocialAuthSDKProvider: NSObject, SocialAuthSDKProvider {
    private typealias CompletionHandler = (Result<SocialAuthSDKResponse, SocialAuthSDKError>) -> Void

    static let shared = AppleIDSocialAuthSDKProvider()

    private var completionHandler: CompletionHandler?

    func signIn() async throws -> SocialAuthSDKResponse {
        let lock = NSLock()

        return try await withCheckedThrowingContinuation { continuation in
            var optionalContinuation: CheckedContinuation<SocialAuthSDKResponse, Error>? = continuation

            signIn { result in
                lock.lock()
                defer { lock.unlock() }

                switch result {
                case .success(let response):
                    optionalContinuation?.resume(returning: response)
                case .failure(let error):
                    optionalContinuation?.resume(throwing: error)
                }

                optionalContinuation = nil
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

// MARK: - AppleIDSocialAuthSDKProvider: ASAuthorizationControllerDelegate -

extension AppleIDSocialAuthSDKProvider: ASAuthorizationControllerDelegate {
    func authorizationController(
        controller: ASAuthorizationController,
        didCompleteWithAuthorization authorization: ASAuthorization
    ) {
        guard let appleIDCredential = authorization.credential as? ASAuthorizationAppleIDCredential else {
            return handleDidCompleteWithError(
                SocialAuthSDKError.connectionError(
                    originalError: AppleIDSocialAuthSDKProviderErrorReason.noASAuthorizationAppleIDCredential
                )
            )
        }

        guard let appleAuthCode = appleIDCredential.authorizationCode else {
            #if DEBUG
            print("AppleIDSocialAuthSDKProvider :: Unable to fetch authorization code")
            #endif
            return handleDidCompleteWithError(
                SocialAuthSDKError.connectionError(
                    originalError: AppleIDSocialAuthSDKProviderErrorReason.noAuthorizationCode
                )
            )
        }

        guard let authCodeString = String(data: appleAuthCode, encoding: .utf8) else {
            #if DEBUG
            print("AppleIDSocialAuthSDKProvider :: Unable to serialize authorization code from data: \(appleAuthCode.debugDescription)")
            #endif
            return handleDidCompleteWithError(
                SocialAuthSDKError.connectionError(
                    originalError: AppleIDSocialAuthSDKProviderErrorReason.failedSerializeAuthorizationCode
                )
            )
        }

        guard let appleIDToken = appleIDCredential.identityToken else {
            #if DEBUG
            print("AppleIDSocialAuthSDKProvider :: Unable to fetch identity token")
            #endif
            return handleDidCompleteWithError(
                SocialAuthSDKError.connectionError(
                    originalError: AppleIDSocialAuthSDKProviderErrorReason.noIdentityToken
                )
            )
        }

        guard let idTokenString = String(data: appleIDToken, encoding: .utf8) else {
            #if DEBUG
            print("AppleIDSocialAuthSDKProvider :: Unable to serialize token string from data: \(appleIDToken.debugDescription)")
            #endif
            return handleDidCompleteWithError(
                SocialAuthSDKError.connectionError(
                    originalError: AppleIDSocialAuthSDKProviderErrorReason.failedSerializeIdentityToken
                )
            )
        }

        let response = SocialAuthSDKResponse(authorizationCode: authCodeString, identityToken: idTokenString)
        completionHandler?(.success(response))
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
                    return .canceled
                case .unknown, .invalidResponse, .notHandled, .failed, .notInteractive:
                    return .connectionError(originalError: error)
                @unknown default:
                    return .connectionError(originalError: error)
                }
            } else {
                return .connectionError(originalError: error)
            }
        }()

        completionHandler?(.failure(sdkError))
    }
}

// MARK: - AppleIDSocialAuthSDKProvider: ASAuthorizationControllerPresentationContextProviding -

extension AppleIDSocialAuthSDKProvider: ASAuthorizationControllerPresentationContextProviding {
    func presentationAnchor(for controller: ASAuthorizationController) -> ASPresentationAnchor {
        SourcelessRouter().window.require()
    }
}
