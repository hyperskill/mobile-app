import Foundation
import shared

protocol StepQuizChildQuizInputProtocol: AnyObject {
    func createReply() -> Reply
    func update(step: Step, dataset: Dataset, reply: Reply?)
}

extension StepQuizChildQuizInputProtocol {
    func update(step: Step, dataset: Dataset, reply: Reply?) {}
}
