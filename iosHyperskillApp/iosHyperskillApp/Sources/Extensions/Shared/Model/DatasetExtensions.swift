import Foundation
import shared

extension Dataset {
    convenience init(
        options: [String]? = nil,
        pairs: [Pair]? = nil,
        rows: [String]? = nil,
        columns: [String]? = nil,
        desc: String? = nil,
        components: [Component]? = nil,
        isMultipleChoice: Bool = false,
        isCheckbox: Bool = false,
        isHtmlEnabled: Bool = false
    ) {
        self.init(
            options: options,
            pairs: pairs,
            rows: rows,
            columns: columns,
            description: desc,
            components: components,
            isMultipleChoice: isMultipleChoice,
            isCheckbox: isCheckbox,
            isHtmlEnabled: isHtmlEnabled
        )
    }
}
