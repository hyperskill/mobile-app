import shared
import SwiftUI

final class ProfileViewModel: FeatureViewModel<
  ProfileFeatureState,
  ProfileFeatureMessage,
  ProfileFeatureActionViewAction
> {
    private let presentationDescription: ProfilePresentationDescription

    private let viewDataMapper: ProfileViewDataMapper

    private let notificationService: NotificationsService
    private let notificationsRegistrationService: NotificationsRegistrationService
    private let notificationInteractor: NotificationInteractor

    init(
        presentationDescription: ProfilePresentationDescription,
        viewDataMapper: ProfileViewDataMapper,
        notificationService: NotificationsService,
        notificationsRegistrationService: NotificationsRegistrationService,
        notificationInteractor: NotificationInteractor,
        feature: Presentation_reduxFeature
    ) {
        self.presentationDescription = presentationDescription
        self.viewDataMapper = viewDataMapper
        self.notificationService = notificationService
        self.notificationsRegistrationService = notificationsRegistrationService
        self.notificationInteractor = notificationInteractor

        super.init(feature: feature)

        NotificationCenter.default.addObserver(
            self,
            selector: #selector(onPermissionStatusUpdate(_:)),
            name: .notificationPermissionStatusDidChange,
            object: nil
        )
    }

    deinit {
        NotificationCenter.default.removeObserver(self)
    }

    func loadProfile(forceUpdate: Bool = false) {
        switch presentationDescription.profileType {
        case .currentUser:
            onNewMessage(ProfileFeatureMessageInit(isInitCurrent: true, profileId: nil, forceUpdate: forceUpdate))
        case .otherUser(let profileUserID):
            onNewMessage(
                ProfileFeatureMessageInit(
                    isInitCurrent: false,
                    profileId: KotlinLong(value: Int64(profileUserID)),
                    forceUpdate: forceUpdate
                )
            )
        }
    }

    func makeViewData(_ profile: Profile) -> ProfileViewData {
        viewDataMapper.mapProfileToViewData(
            profile,
            isDailyStudyRemindersEnabled: notificationInteractor.isDailyStudyRemindersEnabled(),
            dailyStudyRemindersStartHour: Int(notificationInteractor.getDailyStudyRemindersIntervalStartHour())
        )
    }

    // MARK: Presentation

    func presentSocialAccount(_ profileSocialAccount: ProfileSocialAccount) {
        guard let profileURL = profileSocialAccount.profileURL else {
            return
        }

        UIApplication.shared.open(profileURL, options: [:]) { success in
            if !success {
                WebControllerManager.shared.presentWebControllerWithURL(
                    profileURL,
                    withKey: .externalLink,
                    allowsSafari: true,
                    backButtonStyle: .done
                )
            }
        }
    }

    func presentProfileFullVersion() {
        logClickedViewFullProfileEvent()

        guard let contentState = state as? ProfileFeatureStateContent,
              let url = HyperskillURLFactory.makeProfile(id: Int(contentState.profile.id)) else {
            return
        }

        WebControllerManager.shared.presentWebControllerWithURL(
            url,
            withKey: .externalLink,
            allowsSafari: true,
            backButtonStyle: .done
        )
    }

    // MARK: Daily study reminders

    func setDailyStudyRemindersEnabled(_ isEnabled: Bool) {
        logClickedDailyStudyRemindsEvent(isEnabled: isEnabled)

        if isEnabled {
            Task(priority: .userInitiated) {
                let isGranted = await notificationsRegistrationService.requestAuthorizationIfNeeded()

                await MainActor.run {
                    if isGranted {
                        notificationInteractor.setDailyStudyRemindersEnabled(enabled: true)
                        notificationService.scheduleDailyStudyReminderLocalNotifications(
                            analyticRoute: HyperskillAnalyticRoute.Profile()
                        )
                    } else {
                        handleUserDeclinedDailyStudyReminders()
                    }

                    objectWillChange.send()
                }
            }
        } else {
            handleUserDeclinedDailyStudyReminders()
        }
    }

    func setDailyStudyRemindersStartHour(startHour: Int) {
        notificationInteractor.setDailyStudyRemindersIntervalStartHour(hour: Int32(startHour))
        notificationService.scheduleDailyStudyReminderLocalNotifications(
            analyticRoute: HyperskillAnalyticRoute.Profile()
        )
    }

    func determineCurrentNotificationPermissionStatus() {
        guard notificationInteractor.isDailyStudyRemindersEnabled() else {
            return
        }

        Task {
            let permissionStatus = await NotificationPermissionStatus.current

            if permissionStatus == .denied {
                await MainActor.run {
                    handleUserDeclinedDailyStudyReminders(shouldEmitObjectChange: true)
                }
            }
        }
    }

    private func handleUserDeclinedDailyStudyReminders(shouldEmitObjectChange: Bool = false) {
        notificationInteractor.setDailyStudyRemindersEnabled(enabled: false)
        notificationService.removeDailyStudyReminderLocalNotifications()

        if shouldEmitObjectChange {
            objectWillChange.send()
        }
    }

    @objc
    private func onPermissionStatusUpdate(_ notification: Foundation.Notification) {
        guard notificationInteractor.isDailyStudyRemindersEnabled(),
              let permissionStatus = notification.object as? NotificationPermissionStatus else {
            return
        }

        if permissionStatus == .denied {
            handleUserDeclinedDailyStudyReminders(shouldEmitObjectChange: true)
        }
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(ProfileFeatureMessageViewedEventMessage())
    }

    func logClickedSettingsEvent() {
        onNewMessage(ProfileFeatureMessageClickedSettingsEventMessage())
    }

    private func logClickedDailyStudyRemindsEvent(isEnabled: Bool) {
        onNewMessage(ProfileFeatureMessageClickedDailyStudyRemindsEventMessage(isEnabled: isEnabled))
    }

    func logClickedDailyStudyRemindsTimeEvent() {
        onNewMessage(ProfileFeatureMessageClickedDailyStudyRemindsTimeEventMessage())
    }

    private func logClickedViewFullProfileEvent() {
        onNewMessage(ProfileFeatureMessageClickedViewFullProfileEventMessage())
    }
}
