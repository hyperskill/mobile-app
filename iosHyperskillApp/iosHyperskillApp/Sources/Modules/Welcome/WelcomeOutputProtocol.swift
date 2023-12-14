import Foundation

protocol WelcomeOutputProtocol: AnyObject {
    func handleWelcomeSignInRequested()
    func handleWelcomeSignUpRequested(isInSignUpMode: Bool)
}
