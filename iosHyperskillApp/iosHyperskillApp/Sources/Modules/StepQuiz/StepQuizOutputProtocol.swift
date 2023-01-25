import Foundation
import shared

protocol StepQuizOutputProtocol {
    func doContinuePracticing()
    var isPracticingLoading: Bool { get }
}
