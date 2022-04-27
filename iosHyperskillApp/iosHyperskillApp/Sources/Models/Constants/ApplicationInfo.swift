import Foundation
import shared

enum ApplicationInfo {
    static let host = BuildKonfig.shared.HOST

    static let oauthClientID = BuildKonfig.shared.OAUTH_CLIENT_ID
    static let oauthClientSecret = BuildKonfig.shared.OAUTH_CLIENT_SECRET

    static let redirectURI = BuildKonfig.shared.REDIRECT_URI

    static let credentialsClientID = BuildKonfig.shared.CREDENTIALS_CLIEND_ID
    static let credentialsClientSecret = BuildKonfig.shared.CREDENTIALS_CLIENT_SECRET

    static let flavor = BuildKonfig.shared.FLAVOR
}

enum BuildType: String {
    case debug
    case release

    static var current: BuildType {
        #if DEBUG
        return .debug
        #else
        return .release
        #endif
    }
}
