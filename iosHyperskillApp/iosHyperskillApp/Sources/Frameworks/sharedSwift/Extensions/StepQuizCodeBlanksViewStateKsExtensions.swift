import Foundation
import shared

extension StepQuizCodeBlanksViewStateKs: Equatable {
    public static func == (lhs: StepQuizCodeBlanksViewStateKs, rhs: StepQuizCodeBlanksViewStateKs) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle):
            true
        case (.content(let lhsContent), .content(let rhsContent)):
            lhsContent.isEqual(rhsContent)
        case (.content, .idle):
            false
        case (.idle, .content):
            false
        }
    }
}
