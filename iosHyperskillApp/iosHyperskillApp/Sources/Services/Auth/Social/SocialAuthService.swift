import Foundation
import shared

struct SocialAuthResponse {
    let authorizationCode: String
    var identityToken: String?
}

extension SocialAuthResponse {
    init(socialAuthSDKResponse: SocialAuthSDKResponse) {
        self.authorizationCode = socialAuthSDKResponse.authorizationCode
        self.identityToken = socialAuthSDKResponse.identityToken
    }
}

enum SocialAuthError: Error {
    case canceled
    case accessDenied(originalError: Error)
    case connectionError(originalError: Error)
}

protocol SocialAuthServiceProtocol: AnyObject {
    func signIn(with provider: SocialAuthProvider) async throws -> SocialAuthResponse
}

@MainActor
final class SocialAuthService: SocialAuthServiceProtocol {
    static let shared = SocialAuthService()

    private let webOAuthService: WebOAuthServiceProtocol

    private init() {
        self.webOAuthService = WebOAuthService.shared
    }

    func signIn(with provider: SocialAuthProvider) async throws -> SocialAuthResponse {
        if let sdkProvider = provider.sdkProvider {
            return try await self.signIn(sdkProvider: sdkProvider)
        } else if let registerURL = provider.registerURL {
            return try await self.signIn(registerURL: registerURL)
        } else {
            assertionFailure("SocialAuthService :: trying to sign in with unsupported provider = \(provider)")
            throw SocialAuthError.canceled
        }
    }

    // MARK: Private API

    private func signIn(sdkProvider: SocialAuthSDKProvider) async throws -> SocialAuthResponse {
        do {
            let response = try await sdkProvider.signIn()
            return SocialAuthResponse(socialAuthSDKResponse: response)
        } catch {
            guard let sdkError = error as? SocialAuthSDKError else {
                throw SocialAuthError.connectionError(originalError: error)
            }

            switch sdkError {
            case .canceled, .noPresentingViewController:
                throw SocialAuthError.canceled
            case .accessDenied(let originalError):
                throw SocialAuthError.accessDenied(originalError: originalError)
            case .connectionError(let originalError):
                throw SocialAuthError.connectionError(originalError: originalError)
            }
        }
    }

    private func signIn(registerURL: URL) async throws -> SocialAuthResponse {
        do {
            let authCode = try await self.webOAuthService.signIn(registerURL: registerURL)
            return SocialAuthResponse(authorizationCode: authCode)
        } catch {
            guard let webOAuthError = error as? WebOAuthError else {
                throw SocialAuthError.connectionError(originalError: error)
            }

            switch webOAuthError {
            case .canceled, .noPresentingViewController:
                throw SocialAuthError.canceled
            case .accessDenied:
                throw SocialAuthError.accessDenied(originalError: error)
            }
        }
    }
}

// MARK: - SocialAuthProvider -

enum SocialAuthProvider: String, CaseIterable {
    case jetbrains
    case google
    case github
    case apple

    var isSupported: Bool {
        self.registerURL != nil || self.sdkProvider != nil
    }

    var sharedType: shared.SocialAuthProvider {
        switch self {
        case .jetbrains:
            return shared.SocialAuthProvider.jetbrainsAccount
        case .google:
            return shared.SocialAuthProvider.google
        case .github:
            return shared.SocialAuthProvider.github
        case .apple:
            return shared.SocialAuthProvider.apple
        }
    }

    fileprivate var registerURL: URL? {
        let stringOrNil: String?

        switch self {
        case .jetbrains:
            stringOrNil = SocialAuthProviderRequestURLBuilder.shared.build(
                provider: .jetbrainsAccount,
                networkEndpointConfigInfo: AppGraphBridge.sharedAppGraph.networkComponent.endpointConfigInfo
            )
        case .google:
            return nil
        case .github:
            stringOrNil = SocialAuthProviderRequestURLBuilder.shared.build(
                provider: .github,
                networkEndpointConfigInfo: AppGraphBridge.sharedAppGraph.networkComponent.endpointConfigInfo
            )
        case .apple:
            return nil
        }

        guard let string = stringOrNil else {
            return nil
        }

        return URL(string: string)
    }

    fileprivate var sdkProvider: SocialAuthSDKProvider? {
        switch self {
        case .jetbrains:
            return nil
        case .google:
            return GoogleSocialAuthSDKProvider.shared
        case .github:
            return nil
        case .apple:
            return AppleIDSocialAuthSDKProvider.shared
        }
    }
}
