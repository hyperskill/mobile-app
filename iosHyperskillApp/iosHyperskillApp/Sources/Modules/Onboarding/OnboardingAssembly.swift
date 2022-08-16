import Foundation
import shared

final class OnboardingAssembly: Assembly {
    private let onSignInTap: () -> Void
    private let onSignUpTap: () -> Void

    init(onSignInTap: @escaping () -> Void, onSignUpTap: @escaping () -> Void) {
        self.onSignInTap = onSignInTap
        self.onSignUpTap = onSignUpTap
    }

    func makeModule() -> OnboardingView {
        let onboardingComponent = AppGraphBridge.sharedAppGraph.buildOnboardingComponent()

        let viewModel = OnboardingViewModel(
            feature: onboardingComponent.onboardingFeature
        )

        return OnboardingView(viewModel: viewModel, onSignInTap: self.onSignInTap, onSignUpTap: self.onSignUpTap)
    }
}
