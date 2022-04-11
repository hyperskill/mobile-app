import Foundation

final class DeviceInfo {
    static var current = DeviceInfo()

    private init() {}

    var operatingSystemVersion: (major: Int, minor: Int, patch: Int) {
        (
            major: ProcessInfo.processInfo.operatingSystemVersion.majorVersion,
            minor: ProcessInfo.processInfo.operatingSystemVersion.minorVersion,
            patch: ProcessInfo.processInfo.operatingSystemVersion.patchVersion
        )
    }
}
