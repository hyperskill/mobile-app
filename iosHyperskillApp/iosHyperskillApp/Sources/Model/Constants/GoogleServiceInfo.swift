import Foundation

enum GoogleServiceInfo {
    static var clientID: String {
        self.readAndDeserializePlistIntoDictionary()?[Key.clientID.rawValue] as! String
    }

    static var reversedClientID: String {
        self.readAndDeserializePlistIntoDictionary()?[Key.reversedClientID.rawValue] as! String
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
    }
}
