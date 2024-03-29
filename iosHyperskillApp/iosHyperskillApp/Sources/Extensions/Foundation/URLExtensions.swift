import Foundation

// MARK: Methods

extension URL {
    /// URL with appending query parameters.
    ///
    /// - Parameter parameters: parameters dictionary.
    /// - Returns: URL with appending given query parameters.
    func appendingQueryParameters(_ parameters: [String: String]) -> URL? {
        if var urlComponents = URLComponents(url: self, resolvingAgainstBaseURL: true) {
            var items = urlComponents.queryItems ?? []
            items += parameters.map {
                URLQueryItem(name: $0, value: $1)
            }
            urlComponents.queryItems = Array(Set(items))
            return urlComponents.url
        } else {
            return nil
        }
    }

    /// Append query parameters to URL.
    ///
    /// - Parameter parameters: parameters dictionary.
    mutating func appendQueryParameters(_ parameters: [String: String]) {
        if let url = self.appendingQueryParameters(parameters) {
            self = url
        }
    }

    /// Get value of a query key.
    ///
    ///    var url = URL(string: "https://google.com?code=12345")!
    ///    queryValue(for: "code") -> "12345"
    ///
    /// - Parameter key: The key of a query value.
    func queryValue(for key: String) -> String? {
        URLComponents(string: absoluteString)?
            .queryItems?
            .first(where: { $0.name == key })?
            .value
    }
}
