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
    private let notificationInteractor: NotificationInteractor

    init(
        presentationDescription: ProfilePresentationDescription,
        viewDataMapper: ProfileViewDataMapper,
        notificationService: NotificationsService,
        notificationInteractor: NotificationInteractor,
        feature: Presentation_reduxFeature
    ) {
        self.presentationDescription = presentationDescription
        self.viewDataMapper = viewDataMapper
        self.notificationService = notificationService
        self.notificationInteractor = notificationInteractor
        super.init(feature: feature)
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

        notificationInteractor.setDailyStudyRemindersEnabled(enabled: isEnabled)

        if isEnabled {
            notificationService.scheduleDailyStudyReminderLocalNotifications()
            NotificationsRegistrationService.requestAuthorization(grantedHandler: { _ in })
        } else {
            notificationService.removeDailyStudyReminderLocalNotifications()
        }
    }

    func setDailyStudyRemindersStartHour(startHour: Int) {
        notificationInteractor.setDailyStudyRemindersIntervalStartHour(hour: Int32(startHour))
        notificationService.scheduleDailyStudyReminderLocalNotifications()
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(ProfileFeatureMessageProfileViewedEventMessage())
    }

    func logClickedSettingsEvent() {
        onNewMessage(ProfileFeatureMessageProfileClickedSettingsEventMessage())
    }

    private func logClickedDailyStudyRemindsEvent(isEnabled: Bool) {
        onNewMessage(ProfileFeatureMessageProfileClickedDailyStudyRemindsEventMessage(isEnabled: isEnabled))
    }

    func logClickedDailyStudyRemindsTimeEvent() {
        onNewMessage(ProfileFeatureMessageProfileClickedDailyStudyRemindsTimeEventMessage())
    }

    private func logClickedViewFullProfileEvent() {
        onNewMessage(ProfileFeatureMessageProfileClickedViewFullProfileEventMessage())
    }
}
