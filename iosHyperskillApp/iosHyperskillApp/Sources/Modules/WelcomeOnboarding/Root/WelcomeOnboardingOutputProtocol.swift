import Foundation
import shared

protocol WelcomeOnboardingOutputProtocol: AnyObject {
    func handleWelcomeOnboardingCompleted(stepRoute: StepRoute?)
}
