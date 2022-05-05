import Foundation

enum BundlePropertyListDeserializer {
    static func deserializeToDictionary(resourceName: String) -> [String: Any]? {
        guard let plistPath = Bundle.main.url(forResource: resourceName, withExtension: "plist") else {
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
}
