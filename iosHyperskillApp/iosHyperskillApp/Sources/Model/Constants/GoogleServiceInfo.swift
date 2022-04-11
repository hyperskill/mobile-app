import Foundation

enum GoogleServiceInfo {
    static var clientID: String {
        self.getStringValue(for: .clientID).require()
    }

    static var reversedClientID: String {
        self.getStringValue(for: .reversedClientID).require()
    }

    static var serverClientID: String {
        self.getStringValue(for: .serverClientID).require()
    }

    private static func getStringValue(for key: Key) -> String? {
        self.readAndDeserializePlistIntoDictionary()?[key.rawValue] as? String
    }

    private static func readAndDeserializePlistIntoDictionary() -> [String: Any]? {
        guard let plistPath = Bundle.main.url(forResource: "GoogleService-Info", withExtension: "plist") else {
            return nil
        }

        do {
            let plistData = try Data(contentsOf: plistPath)

            guard let dict = try PropertyListSerialization.propertyList(
                from: plistData,
                options: [],
                format: nil
            ) as? [String: Any] else {
                return nil
            }

            return dict
        } catch {
            return nil
        }
    }

    enum Key: String {
        case clientID = "CLIENT_ID"
        case reversedClientID = "REVERSED_CLIENT_ID"
        case serverClientID = "SERVER_CLIENT_ID"
    }
}
