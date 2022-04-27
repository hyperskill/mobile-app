import Foundation

enum GoogleServiceInfo {
    private static let plistDictionary = BundlePropertyListDeserializer.deserializeToDictionary(
        resourceName: "GoogleService-Info"
    )

    static let clientID = Self.getStringValue(for: .clientID).require()

    static let reversedClientID = Self.getStringValue(for: .reversedClientID).require()

    static let serverClientID = Self.getStringValue(for: .serverClientID).require()

    private static func getStringValue(for key: Key) -> String? {
        self.plistDictionary?[key.rawValue] as? String
    }

    enum Key: String {
        case clientID = "CLIENT_ID"
        case reversedClientID = "REVERSED_CLIENT_ID"
        case serverClientID = "SERVER_CLIENT_ID"
    }
}
