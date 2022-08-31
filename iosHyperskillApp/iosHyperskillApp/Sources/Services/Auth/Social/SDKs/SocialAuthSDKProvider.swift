import Foundation

protocol SocialAuthSDKProvider: AnyObject {
    func signIn() async throws -> SocialAuthSDKResponse
}

struct SocialAuthSDKResponse {
    let authorizationCode: String
    var identityToken: String?
}

enum SocialAuthSDKError: Error {
    case canceled
    case accessDenied
    case connectionError
    case noPresentingViewController
}
