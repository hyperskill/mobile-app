import shared
import SwiftUI

final class ProfileViewModel: FeatureViewModel<
  ProfileFeatureState,
  ProfileFeatureMessage,
  ProfileFeatureActionViewAction
> {
    private let presentationDescription: ProfilePresentationDescription

    private let viewDataMapper: ProfileViewDataMapper

    private let notificationInteractor: NotificationInteractor

    private let notificationService: NotificationsService

    init(
        presentationDescription: ProfilePresentationDescription,
        viewDataMapper: ProfileViewDataMapper,
        notificationInteractor: NotificationInteractor,
        notificationService: NotificationsService,
        feature: Presentation_reduxFeature
    ) {
        self.presentationDescription = presentationDescription
        self.viewDataMapper = viewDataMapper
        self.notificationInteractor = notificationInteractor
        self.notificationService = notificationService
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
        viewDataMapper.mapProfileToViewData(profile)
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

    func isDailyStudyRemindersEnabled() -> Bool {
        self.notificationInteractor.isDailyStudyRemindersEnabled()
    }

    func setDailyStudyRemindersActivated(isActivated: Bool) async {
        self.notificationInteractor.setDailyStudyRemindersEnabled(enabled: isActivated)
        if isActivated {
            await self.scheduleNotifications()
        } else {
            notificationService.removeDailyStudyReminderLocalNotifications(
                notificationsCount: notificationInteractor
                    .getShuffledDailyStudyRemindersNotificationDescriptions()
                    .count
            )
        }
    }

    func getDailyStudyRemindersStartHour() -> Int {
        Int(self.notificationInteractor.getDailyStudyRemindersIntervalStartHour())
    }

    func setDailyStudyRemindersStartHour(startHour: Int) async {
        self.notificationInteractor.setDailyStudyRemindersIntervalStartHour(hour: Int32(startHour))
        await self.scheduleNotifications()
    }

    private func scheduleNotifications() async {
        await notificationService.scheduleDailyStudyReminderLocalNotifications(
            notificationDescriptions: notificationInteractor.getShuffledDailyStudyRemindersNotificationDescriptions(),
            startHour: Int(notificationInteractor.getDailyStudyRemindersIntervalStartHour())
        )
    }
}
