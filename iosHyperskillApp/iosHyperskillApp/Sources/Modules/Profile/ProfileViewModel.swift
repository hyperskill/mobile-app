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

    private let notificationService: LocalNotificationsService

    init(
        presentationDescription: ProfilePresentationDescription,
        viewDataMapper: ProfileViewDataMapper,
        notificationInteractor: NotificationInteractor,
        notificationService: LocalNotificationsService,
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

    func isRemindersActivated() -> Bool {
        self.notificationInteractor.isNotificationsEnabled()
    }

    func setRemindersActivated(activated: Bool) async {
        self.notificationInteractor.setNotificationsEnabled(enabled: activated)
        await self.scheduleNotification()
    }

    func getRemindersSelectedHour() -> Int {
        Int(self.notificationInteractor.getDailyStudyRemindersIntervalStartHour())
    }

    func setRemindersSelectedHour(selectedHour: Int) async {
        self.notificationInteractor.setDailyStudyRemindersIntervalStartHour(hour: Int32(selectedHour))
        await self.scheduleNotification()
    }

    private func scheduleNotification() async {
        try? await notificationService.scheduleNotification(
            ReminderLocalNotification(
                notificationDescritpion: notificationInteractor.getRandomNotificationDescription(),
                selectedHour: Int(notificationInteractor.getDailyStudyRemindersIntervalStartHour())
            )
        )
    }
}
