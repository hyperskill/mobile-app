import SwiftUI

@available(iOS, introduced: 13, deprecated: 15, message: "Use textSelection(_:) directly")
extension View {
    @ViewBuilder
    func textSelectionCompatibility(fallbackStringToCopy string: String) -> some View {
        if #available(iOS 15.0, *) {
            textSelection(.enabled)
        } else {
            contextMenu {
                Button(Strings.StepQuiz.Hints.copy) {
                    UIPasteboard.general.string = string
                }
            }
        }
    }
}
