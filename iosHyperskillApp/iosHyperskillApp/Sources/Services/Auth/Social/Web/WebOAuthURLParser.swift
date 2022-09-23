import Foundation

enum WebOAuthURLParser {
    static func isRedirectURI(_ url: URL) -> Bool {
        guard url.absoluteString.starts(with: ApplicationInfo.redirectURI) else {
            return false
        }

        guard self.getCodeQueryValue(url) != nil else {
            return false
        }

        return true
    }

    static func getCodeQueryValue(_ url: URL) -> String? {
        url.queryValue(for: Key.code.rawValue)
    }

    enum Key: String {
        case code
    }
}
