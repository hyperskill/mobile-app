import Foundation
import shared

enum ApplicationInfo {
    private static let buildKonfig = AppGraphBridge.sharedAppGraph.commonComponent.buildKonfig
    private static let endpointConfigInfo = AppGraphBridge.sharedAppGraph.networkComponent.endpointConfigInfo

    static let host = endpointConfigInfo.host

    static let oauthClientID = endpointConfigInfo.oauthClientId
    static let oauthClientSecret = endpointConfigInfo.oauthClientSecret

    static let redirectURI = endpointConfigInfo.redirectUri

    static let credentialsClientID = endpointConfigInfo.credentialsClientId
    static let credentialsClientSecret = endpointConfigInfo.credentialsClientSecret

    static let flavor = buildKonfig.flavor

    static let isDebugModeAvailable: Bool = DebugFeature.shared.isAvailable(buildKonfig: buildKonfig)
}

enum BuildType: String {
    case debug
    case release

    static var current: BuildType {
        #if DEBUG
        return .debug
        #else
        return .release
        #endif
    }

    var buildVariant: BuildVariant {
        switch self {
        case .debug:
            return BuildVariant.debug
        case .release:
            return BuildVariant.release_
        }
    }
}
