import Foundation
import shared

final class NotificationsOnboardingViewModel: FeatureViewModel<
  NotificationsOnboardingFeature.ViewState,
  NotificationsOnboardingFeatureMessage,
  NotificationsOnboardingFeatureActionViewAction
> {
    weak var moduleOutput: NotificationsOnboardingOutputProtocol?

    private let notificationsRegistrationService: NotificationsRegistrationService

    init(
        notificationsRegistrationService: NotificationsRegistrationService,
        feature: Presentation_reduxFeature
    ) {
        self.notificationsRegistrationService = notificationsRegistrationService
        super.init(feature: feature)
    }

    override func shouldNotifyStateDidChange(
        oldState: NotificationsOnboardingFeature.ViewState,
        newState: NotificationsOnboardingFeature.ViewState
    ) -> Bool {
        !oldState.isEqual(newState)
    }

    func doPrimaryAction() {
        onNewMessage(NotificationsOnboardingFeatureMessageAllowNotificationsClicked())
    }

    func doSecondaryAction() {
        onNewMessage(NotificationsOnboardingFeatureMessageNotNowClicked())
    }

    func doRequestNotificationPermission() {
        Task(priority: .userInitiated) {
            let isGranted = await notificationsRegistrationService.requestAuthorizationIfNeeded()

            await MainActor.run {
                onNewMessage(
                    NotificationsOnboardingFeatureMessageNotificationPermissionRequestResult(
                        isPermissionGranted: isGranted
                    )
                )
            }
        }
    }

    func doCompleteOnboarding() {
        moduleOutput?.handleNotificationsOnboardingCompleted()
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(NotificationsOnboardingFeatureMessageViewedEventMessage())
    }
}
