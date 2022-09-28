import Foundation
import shared

protocol StepQuizChildQuizOutputProtocol: AnyObject {
    func handleChildQuizSync(reply: Reply)
    func handleChildQuizSubmitCurrentReply()
    func handleChildQuizRetry()

    func handleChildQuizAnalyticEventMessage(_ message: StepQuizFeatureMessage)
}
