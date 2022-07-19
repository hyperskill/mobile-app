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
            versionName: MainBundleInfo.versionName,
            osVersion: osVersion,
            versionCode: MainBundleInfo.versionCode,
            applicationId: Bundle.main.bundleIdentifier ?? "N/A"
        )
    }
}
