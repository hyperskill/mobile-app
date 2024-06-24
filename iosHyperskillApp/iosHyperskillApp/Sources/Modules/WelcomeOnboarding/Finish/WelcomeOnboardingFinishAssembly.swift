import Foundation
import shared
import SwiftUI

final class WelcomeOnboardingFinishAssembly: Assembly {
    private let track: WelcomeOnboardingTrack

    private let onViewDidAppear: () -> Void
    private let onCallToActionButtonTap: () -> Void

    init(
        track: WelcomeOnboardingTrack,
        onViewDidAppear: @escaping () -> Void,
        onCallToActionButtonTap: @escaping () -> Void
    ) {
        self.track = track
        self.onViewDidAppear = onViewDidAppear
        self.onCallToActionButtonTap = onCallToActionButtonTap
    }

    func makeModule() -> WelcomeOnboardingFinishView {
        let welcomeOnboardingFinishComponent = AppGraphBridge.sharedAppGraph.buildWelcomeOnboardingFinishComponent()

        let welcomeOnboardingFinishViewStateMapper =
            welcomeOnboardingFinishComponent.welcomeOnboardingFinishViewStateMapper
        let welcomeOnboardingFinishViewState = welcomeOnboardingFinishViewStateMapper.map(track: track)

        return WelcomeOnboardingFinishView(
            viewState: welcomeOnboardingFinishViewState,
            onViewDidAppear: onViewDidAppear,
            onCallToActionButtonTap: onCallToActionButtonTap
        )
    }
}
