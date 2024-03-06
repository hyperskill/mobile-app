import Foundation

enum RevenueCatInfo {
    private static let plistDictionary = BundlePropertyListDeserializer.deserializeToDictionary(
        resourceName: "RevenueCat-Info"
    )

    static let publicAPIKey = Self.getStringValue(for: .publicAPIKey).require()

    private static func getStringValue(for key: Key) -> String? {
        self.plistDictionary?[key.rawValue] as? String
    }

    enum Key: String {
        case publicAPIKey = "PUBLIC_API_KEY"
    }
}
