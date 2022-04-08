import Foundation

enum UserAgentBuilder {
    static var userAgent: String {
        guard let bundleID = Bundle.main.bundleIdentifier,
              let version = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String,
              let build = Bundle.main.infoDictionary?[kCFBundleVersionKey as String] as? String else {
            return "Hyperskill-Mobile (iOS)"
        }

        let osVersion = [
            "\(DeviceInfo.current.operatingSystemVersion.major)",
            "\(DeviceInfo.current.operatingSystemVersion.minor)",
            "\(DeviceInfo.current.operatingSystemVersion.patch)"
        ].joined(separator: ".")

        return "Hyperskill-Mobile/\(version) (\(bundleID); build \(build); iOS \(osVersion))"
    }
}
