import Foundation
import shared

final class OnboardingViewModel: FeatureViewModel<
OnboardingFeatureState,
OnboardingFeatureMessage,
OnboardingFeatureActionViewAction
> {
    func loadOnboarding() {
        onNewMessage(OnboardingFeatureMessageInit())
    }
}
