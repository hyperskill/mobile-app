import Foundation
import shared

enum UserAgentBuilder {
    static var userAgentInfo: UserAgentInfo {
        let osVersion: String = {
            let formattedOperatingSystemVersion = [
                "\(DeviceInfo.current.operatingSystemVersion.major)",
                "\(DeviceInfo.current.operatingSystemVersion.minor)",
                "\(DeviceInfo.current.operatingSystemVersion.patch)"
            ].joined(separator: ".")
            return "iOS \(formattedOperatingSystemVersion)"
        }()

        return UserAgentInfo(
            versionName: Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String ?? "N/A",
            osVersion: osVersion,
            versionCode: Bundle.main.infoDictionary?[kCFBundleVersionKey as String] as? String ?? "N/A",
            applicationId: Bundle.main.bundleIdentifier ?? "N/A"
        )
    }
}
