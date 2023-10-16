import Foundation

enum AppsFlyerInfo {
    private static let plistDictionary = BundlePropertyListDeserializer.deserializeToDictionary(
        resourceName: "AppsFlyer-Info"
    )

    static let appleAppID = Self.getStringValue(for: .appleAppID).require()

    private static func getStringValue(for key: Key) -> String? {
        self.plistDictionary?[key.rawValue] as? String
    }

    enum Key: String {
        case appleAppID = "APPLE_APP_ID"
    }
}
