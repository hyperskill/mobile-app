import Foundation

enum SentryInfo {
    private static let plistDictionary = BundlePropertyListDeserializer.deserializeToDictionary(
        resourceName: "Sentry-Info"
    )

    static let dsn = Self.getStringValue(for: .dsn).require()

    private static func getStringValue(for key: Key) -> String? {
        self.plistDictionary?[key.rawValue] as? String
    }

    enum Key: String {
        case dsn = "PUBLIC_DSN"
    }
}
