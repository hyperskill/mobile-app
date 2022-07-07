import Foundation
import Nuke

enum NukeManager {
    static func registerCustomDecoders() {
        ImageDecoderRegistry.shared.register { context in
            let isSVG = context.urlResponse?.url?.absoluteString.hasSuffix(".svg") ?? false
            return isSVG ? ImageDecoders.SVG() : nil
        }
    }
}
