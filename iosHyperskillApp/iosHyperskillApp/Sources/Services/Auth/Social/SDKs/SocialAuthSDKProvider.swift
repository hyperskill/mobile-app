import Foundation

protocol SocialAuthSDKProvider: AnyObject {
    func signIn() async throws -> SocialAuthSDKResponse
}

struct SocialAuthSDKResponse {
    let token: String
}

enum SocialAuthSDKError: Error {
    case canceled
    case accessDenied
    case connectionError
    case noPresentingViewController
}
