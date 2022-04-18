import Foundation
import shared

struct SocialAuthResponse {
    var authCode: String?
    var socialToken: String?
}

enum SocialAuthError: Error {
    case canceled
    case accessDenied
    case connectionError
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
            return SocialAuthResponse(socialToken: response.token)
        } catch {
            guard let sdkError = error as? SocialAuthSDKError else {
                throw SocialAuthError.connectionError
            }

            switch sdkError {
            case .canceled, .noPresentingViewController:
                throw SocialAuthError.canceled
            case .accessDenied:
                throw SocialAuthError.accessDenied
            case .connectionError:
                throw SocialAuthError.connectionError
            }
        }
    }

    private func signIn(registerURL: URL) async throws -> SocialAuthResponse {
        do {
            let authCode = try await self.webOAuthService.signIn(registerURL: registerURL)
            return SocialAuthResponse(authCode: authCode)
        } catch {
            guard let webOAuthError = error as? WebOAuthError else {
                throw SocialAuthError.connectionError
            }

            switch webOAuthError {
            case .canceled, .noPresentingViewController:
                throw SocialAuthError.canceled
            case .accessDenied:
                throw SocialAuthError.accessDenied
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
            stringOrNil = SocialAuthProviderRequestURLBuilder.shared.build(provider: .jetbrainsAccount)
        case .google:
            return nil
        case .github:
            stringOrNil = SocialAuthProviderRequestURLBuilder.shared.build(provider: .github)
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
            return nil
        }
    }
}
