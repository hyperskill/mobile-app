import Foundation
import Nuke
import SVGKit

extension ImageDecoders {
    final class SVG: ImageDecoding {
        var isAsynchronous: Bool { true }

        func decode(_ data: Data) -> ImageContainer? {
            guard let svgImage = SVGKImage(data: data) else {
                return nil
            }

            return ImageContainer(image: svgImage.uiImage, type: .svg, data: data, userInfo: [:])
        }

        func decodePartiallyDownloadedData(_ data: Data) -> ImageContainer? { nil }
    }
}

extension AssetType {
    static let svg: AssetType = "org.hyperskill.svg"
}
