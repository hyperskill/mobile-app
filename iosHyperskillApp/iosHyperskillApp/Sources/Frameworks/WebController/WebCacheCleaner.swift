import Foundation
import WebKit

enum WebCacheCleaner {
    static func clean(completionHandler: @escaping () -> Void = {}) {
        HTTPCookieStorage.shared.removeCookies(since: .distantPast)

        WKWebsiteDataStore.default().removeData(
            ofTypes: WKWebsiteDataStore.allWebsiteDataTypes(),
            modifiedSince: .distantPast,
            completionHandler: completionHandler
        )
    }
}
