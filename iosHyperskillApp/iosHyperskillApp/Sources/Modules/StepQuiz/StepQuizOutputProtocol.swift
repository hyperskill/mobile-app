import Foundation
import shared

protocol StepQuizOutputProtocol {
    func doContinuePracticing(currentStep: Step)
    var isPracticingLoading: Bool { get }
}
