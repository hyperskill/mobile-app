import Foundation

enum MainBundleInfo {
    static var versionName: String {
        Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String ?? "N/A"
    }

    static var versionCode: String {
        Bundle.main.infoDictionary?[kCFBundleVersionKey as String] as? String ?? "N/A"
    }
}
