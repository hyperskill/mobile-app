import Foundation
import shared

final class OnboardingViewModel: FeatureViewModel<
  OnboardingFeatureState,
  OnboardingFeatureMessage,
  OnboardingFeatureActionViewAction
> {
    weak var moduleOutput: OnboardingOutputProtocol?

    func loadOnboarding() {
        onNewMessage(OnboardingFeatureMessageInit())
    }

    func doAuthPresentation() {
        moduleOutput?.handleOnboardingSignInRequested()
    }

    func doNewUserPresentation() {
        moduleOutput?.handleOnboardingSignUpRequested()
    }
}
