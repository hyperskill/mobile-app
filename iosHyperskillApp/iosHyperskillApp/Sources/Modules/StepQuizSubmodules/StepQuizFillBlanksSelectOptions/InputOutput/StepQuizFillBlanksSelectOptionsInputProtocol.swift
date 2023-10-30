import Foundation

protocol StepQuizFillBlanksSelectOptionsInputProtocol: AnyObject {
    func update(options: [StepQuizFillBlankOption], selectedIndices: Set<Int>, blanksCount: Int)
}
