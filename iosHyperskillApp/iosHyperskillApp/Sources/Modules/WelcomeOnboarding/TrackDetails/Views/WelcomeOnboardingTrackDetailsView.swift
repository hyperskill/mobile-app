import shared
import SwiftUI

extension WelcomeOnboardingTrackDetailsView {
    struct Appearance {
        let backgroundColor = Color.systemGroupedBackground
    }
}

struct WelcomeOnboardingTrackDetailsView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: WelcomeOnboardingTrackDetailsViewModel

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: appearance.backgroundColor)

            WelcomeOnboardingTrackDetailsContentView(
                title: viewModel.state.title,
                trackImageResource: viewModel.state.track.imageResource,
                trackTitle: viewModel.state.trackTitle,
                trackDescription: viewModel.state.trackDescriptionHtml,
                subtitle: viewModel.state.changeText,
                callToActionButtonTitle: viewModel.state.buttonText,
                onCallToActionButtonTap: viewModel.doCallToAction
            )

            if viewModel.state.isLoadingShowed {
                let _ = ProgressHUD.show()
            } else {
                let _ = ProgressHUD.dismissWithDelay()
            }
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear {
            viewModel.stopListening()
            viewModel.onViewAction = nil
        }
    }
}

private extension WelcomeOnboardingTrack {
    // swiftlint:disable switch_case_on_newline
    var imageResource: ImageResource? {
        switch self {
        case .java: .welcomeOnboardingTrackDetailsJava
        case .javaScript: .welcomeOnboardingTrackDetailsJs
        case .kotlin: .welcomeOnboardingTrackDetailsKotlin
        case .python: .welcomeOnboardingTrackDetailsPython
        case .sql: .welcomeOnboardingTrackDetailsSql
        default: nil
        }
    }
    // swiftlint:enable switch_case_on_newline
}

// MARK: - WelcomeOnboardingTrackDetailsView (ViewAction) -

private extension WelcomeOnboardingTrackDetailsView {
    func handleViewAction(
        _ viewAction: WelcomeOnboardingTrackDetailsFeatureActionViewAction
    ) {
        switch WelcomeOnboardingTrackDetailsFeatureActionViewActionKs(viewAction) {
        case .notifyTrackSelected:
            ProgressHUD.showSuccess()
            viewModel.doNotifyTrackSelected()
        case .showTrackSelectionError:
            ProgressHUD.showError(status: Strings.Common.error)
        }
    }
}
