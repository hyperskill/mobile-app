import Foundation
import shared

enum ApplicationInfo {
    static let host = BuildConfig.shared.HOST

    static let oauthClientID = BuildConfig.shared.OAUTH_CLIENT_ID
    static let oauthClientSecret = BuildConfig.shared.OAUTH_CLIENT_SECRET

    static let redirectURI = BuildConfig.shared.REDIRECT_URI

    static let credentialsClientID = BuildConfig.shared.CREDENTIALS_CLIEND_ID
    static let credentialsClientSecret = BuildConfig.shared.CREDENTIALS_CLIENT_SECRET
}
