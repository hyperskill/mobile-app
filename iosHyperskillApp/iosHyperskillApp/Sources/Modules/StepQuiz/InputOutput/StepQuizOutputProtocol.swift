import Foundation

protocol StepQuizOutputProtocol: AnyObject {
    func stepQuizDidRequestContinue()
    func stepQuizDidRequestShowComments()
    func stepQuizDidRequestSkipStep()
}
