import UIKit

final class DeviceInfo {
    static var current = DeviceInfo()

    private init() {}

    var isPad: Bool {
        UIDevice.current.userInterfaceIdiom == .pad
    }

    var operatingSystemVersion: (major: Int, minor: Int, patch: Int) {
        (
            major: ProcessInfo.processInfo.operatingSystemVersion.majorVersion,
            minor: ProcessInfo.processInfo.operatingSystemVersion.minorVersion,
            patch: ProcessInfo.processInfo.operatingSystemVersion.patchVersion
        )
    }
}
