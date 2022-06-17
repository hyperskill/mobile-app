import Foundation
import shared

protocol StepQuizChildQuizDelegate: AnyObject {
    func handleChildQuizSync(reply: Reply)
    func handleChildQuizSubmit(reply: Reply)
}
