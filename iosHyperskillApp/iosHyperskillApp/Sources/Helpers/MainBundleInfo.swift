import Foundation

enum MainBundleInfo {
    static var identifier: String? { Bundle.main.bundleIdentifier }

    // MARK: Info Dictionary

    private static var infoDictionary: [String: Any]? { Bundle.main.infoDictionary }

    static var shortVersionString: String? {
        infoDictionary?["CFBundleShortVersionString"] as? String
    }

    static var buildNumberString: String? {
        infoDictionary?[kCFBundleVersionKey as String] as? String
    }

    /// Returns formatted version with build number string -> "1.0 (1)".
    /// Otherwise returns `nil` if missing `shortVersionString` or `buildNumberString`.
    static var shortVersionWithBuildNumberString: String? {
        guard let shortVersionString,
              let buildNumberString else {
            return nil
        }

        return "\(shortVersionString) (\(buildNumberString))"
    }
}
