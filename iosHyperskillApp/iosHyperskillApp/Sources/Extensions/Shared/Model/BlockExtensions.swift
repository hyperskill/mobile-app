import Foundation
import shared

extension Block {
    convenience init(
        name: String? = nil,
        text: String? = nil,
        options: Options = .init()
    ) {
        self.init(
            name: name ?? "",
            text: text ?? "",
            options: options
        )
    }
}
