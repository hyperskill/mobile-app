import Foundation

protocol OnboardingOutputProtocol: AnyObject {
    func handleOnboardingSignInRequested()

    func handleOnboardingSignUpRequested()
}
