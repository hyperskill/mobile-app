import Foundation

enum GoogleServiceInfo {
    private static let plistDictionary = BundlePropertyListDeserializer.deserializeToDictionary(
        resourceName: "GoogleService-Info"
    )

    static let authClientID = Self.getStringValue(for: .authClientID).require()

    static let authReversedClientID = Self.getStringValue(for: .authReversedClientID).require()

    static let authServerClientID = Self.getStringValue(for: .authServerClientID).require()

    private static func getStringValue(for key: Key) -> String? {
        self.plistDictionary?[key.rawValue] as? String
    }

    enum Key: String {
        case authClientID = "AUTH_CLIENT_ID"
        case authReversedClientID = "AUTH_REVERSED_CLIENT_ID"
        case authServerClientID = "AUTH_SERVER_CLIENT_ID"
    }
}
