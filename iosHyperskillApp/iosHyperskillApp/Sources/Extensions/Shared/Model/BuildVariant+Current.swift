import Foundation
import shared

extension BuildVariant {
    static var current: BuildVariant {
        let isInternalTesting = BuildKonfig.companion.IS_INTERNAL_TESTING?.boolValue ?? false

        if isInternalTesting {
            return .internalRelease
        }

        #if DEBUG
        return .debug
        #else
        return .release_
        #endif
    }
}
