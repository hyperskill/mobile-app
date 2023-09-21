import shared
import SwiftUI

extension NotificationsOnboardingView {
    struct Appearance {
        let backgroundColor = Color.background
    }
}

struct NotificationsOnboardingView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: NotificationsOnboardingViewModel

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: appearance.backgroundColor)

            NotificationsOnboardingContentView(
                onPrimaryButtonTap: viewModel.doPrimaryAction,
                onSecondaryButtonTap: viewModel.doSecondaryAction
            )
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

// MARK: - NotificationsOnboardingView (ViewAction) -

private extension NotificationsOnboardingView {
    func handleViewAction(
        _ viewAction: NotificationsOnboardingFeatureActionViewAction
    ) {
        switch NotificationsOnboardingFeatureActionViewActionKs(viewAction) {
        case .completeNotificationOnboarding:
            #warning("TODO")
        case .requestNotificationPermission:
            viewModel.doRequestNotificationPermission()
        }
    }
}

// MARK: - NotificationsOnboardingView_Previews: PreviewProvider -

struct NotificationsOnboardingView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            NotificationsOnboardingAssembly().makeModule()
        }
    }
}
