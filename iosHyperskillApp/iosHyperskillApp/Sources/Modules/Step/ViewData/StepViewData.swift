import Foundation
import shared

// MARK: Step

struct StepViewData {
    let title: String

    let formattedTimeToComplete: String

    var text: String
}

#if DEBUG
extension StepViewData {
    static var placeholder: StepViewData {
        StepViewData(
            title: "Introduction to Kotlin",
            formattedTimeToComplete: "6 minutes remaining",
            text: """
<h5 style=\"text-align: center;\">What is Kotlin?</h5>\n\n<p><strong>Kotlin</strong> is a highly effective modern \
programming language developed by <a target=\"_blank\" href=\"https://www.jetbrains.com/\" \
rel=\"noopener noreferrer nofollow\">JetBrains</a>. It has a very clear and concise syntax, which makes your code easy \
to read.
"""
        )
    }
}
#endif
