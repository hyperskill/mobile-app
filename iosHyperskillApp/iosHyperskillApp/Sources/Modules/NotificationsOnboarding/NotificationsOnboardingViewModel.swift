import Foundation
import shared

final class NotificationsOnboardingViewModel: FeatureViewModel<
  NotificationsOnboardingFeature.State,
  NotificationsOnboardingFeatureMessage,
  NotificationsOnboardingFeatureActionViewAction
> {
    private let notificationsRegistrationService: NotificationsRegistrationService

    init(
        notificationsRegistrationService: NotificationsRegistrationService,
        feature: Presentation_reduxFeature
    ) {
        self.notificationsRegistrationService = notificationsRegistrationService
        super.init(feature: feature)
    }

    override func shouldNotifyStateDidChange(
        oldState: NotificationsOnboardingFeature.State,
        newState: NotificationsOnboardingFeature.State
    ) -> Bool {
        false
    }

    func doPrimaryAction() {
        onNewMessage(NotificationsOnboardingFeatureMessageAllowNotificationClicked())
    }

    func doSecondaryAction() {
        onNewMessage(NotificationsOnboardingFeatureMessageRemindMeLaterClicked())
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

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(NotificationsOnboardingFeatureMessageViewedEventMessage())
    }
}
