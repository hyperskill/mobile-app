import Foundation
import shared

protocol StepOutputProtocol: AnyObject {
    func handleStepReloading(stepRoute: StepRoute)
}
