import shared
import SwiftUI

final class WelcomeOnboardingTrackDetailsAssembly: UIKitAssembly {
    private let track: WelcomeOnboardingTrack

    private weak var moduleOutput: WelcomeOnboardingTrackDetailsOutputProtocol?

    init(track: WelcomeOnboardingTrack, moduleOutput: WelcomeOnboardingTrackDetailsOutputProtocol?) {
        self.track = track
        self.moduleOutput = moduleOutput
    }

    func makeModule() -> UIViewController {
        let welcomeOnboardingTrackDetailsComponent =
            AppGraphBridge.sharedAppGraph.buildWelcomeOnboardingTrackDetailsComponent(track: track)

        let welcomeOnboardingTrackDetailsViewModel = WelcomeOnboardingTrackDetailsViewModel(
            feature: welcomeOnboardingTrackDetailsComponent.welcomeOnboardingTrackDetailsFeature
        )
        welcomeOnboardingTrackDetailsViewModel.moduleOutput = moduleOutput

        let welcomeOnboardingTrackDetailsView = WelcomeOnboardingTrackDetailsView(
            viewModel: welcomeOnboardingTrackDetailsViewModel
        )

        let hostingController = StyledHostingController(
            rootView: welcomeOnboardingTrackDetailsView
        )
        hostingController.navigationItem.largeTitleDisplayMode = .never

        return hostingController
    }
}
