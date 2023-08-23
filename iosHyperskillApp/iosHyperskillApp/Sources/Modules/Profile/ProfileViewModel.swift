import shared
import SwiftUI

final class ProfileViewModel: FeatureViewModel<
  ProfileFeatureState,
  ProfileFeatureMessage,
  ProfileFeatureActionViewAction
> {
    private let presentationDescription: ProfilePresentationDescription

    private let profileViewDataMapper: ProfileViewDataMapper

    private let badgesViewStateMapper: BadgesViewStateMapper

    private let notificationService: NotificationsService
    private let notificationsRegistrationService: NotificationsRegistrationService
    private let notificationInteractor: NotificationInteractor

    private var currentNotificationPermissionStatus: NotificationPermissionStatus? {
        didSet {
            handleCurrentNotificationPermissionStatusDidSet(
                oldValue: oldValue,
                newValue: currentNotificationPermissionStatus
            )
        }
    }

    var stateKs: ProfileFeatureStateKs { .init(state) }

    init(
        presentationDescription: ProfilePresentationDescription,
        profileViewDataMapper: ProfileViewDataMapper,
        badgesViewStateMapper: BadgesViewStateMapper,
        notificationService: NotificationsService,
        notificationsRegistrationService: NotificationsRegistrationService,
        notificationInteractor: NotificationInteractor,
        feature: Presentation_reduxFeature
    ) {
        self.presentationDescription = presentationDescription
        self.profileViewDataMapper = profileViewDataMapper
        self.badgesViewStateMapper = badgesViewStateMapper
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

    override func shouldNotifyStateDidChange(oldState: ProfileFeatureState, newState: ProfileFeatureState) -> Bool {
        ProfileFeatureStateKs(oldState) != ProfileFeatureStateKs(newState)
    }

    func doLoadProfile(forceUpdate: Bool = false) {
        switch presentationDescription.profileType {
        case .currentUser:
            onNewMessage(ProfileFeatureMessageInitialize(isInitCurrent: true, profileId: nil, forceUpdate: forceUpdate))
        case .otherUser(let profileUserID):
            onNewMessage(
                ProfileFeatureMessageInitialize(
                    isInitCurrent: false,
                    profileId: KotlinLong(value: Int64(profileUserID)),
                    forceUpdate: forceUpdate
                )
            )
        }
    }

    func doPullToRefresh() {
        switch presentationDescription.profileType {
        case .currentUser:
            onNewMessage(ProfileFeatureMessagePullToRefresh(isRefreshCurrent: true, profileId: nil))
        case .otherUser(let profileUserID):
            onNewMessage(
                ProfileFeatureMessagePullToRefresh(
                    isRefreshCurrent: false,
                    profileId: KotlinLong(value: Int64(profileUserID))
                )
            )
        }
    }

    func makeProfileViewData(
        profile: Profile,
        dailyStudyRemindersState: ProfileFeatureDailyStudyRemindersState
    ) -> ProfileViewData {
        profileViewDataMapper.mapProfileToViewData(
            profile,
            isDailyStudyRemindersEnabled: dailyStudyRemindersState.isEnabled,
            dailyStudyRemindersStartHour: Int(dailyStudyRemindersState.startHour),
            notificationPermissionStatus: currentNotificationPermissionStatus
        )
    }

    func makeBadgesViewState(
        badgesState: ProfileFeatureBadgesState
    ) -> BadgesViewState {
        badgesViewStateMapper.map(state: badgesState)
    }

    // MARK: Presentation

    func doSocialAccountPresentation(_ profileSocialAccount: ProfileSocialAccount) {
        guard let profileURL = profileSocialAccount.profileURL else {
            return
        }

        UIApplication.shared.open(profileURL, options: [:]) { success in
            if !success {
                WebControllerManager.shared.presentWebControllerWithURL(
                    profileURL,
                    withKey: .externalLink,
                    controllerType: .inAppSafari,
                    backButtonStyle: .done
                )
            }
        }
    }

    func doProfileFullVersionPresentation() {
        onNewMessage(ProfileFeatureMessageClickedViewFullProfile())
    }

    // MARK: Streak freeze

    func doStreakFreezeCardButtonTapped() {
        onNewMessage(ProfileFeatureMessageStreakFreezeCardButtonClicked())
    }

    func doStreakFreezeModalButtonTapped() {
        onNewMessage(ProfileFeatureMessageStreakFreezeModalButtonClicked())
    }

    // MARK: Badges

    func doBadgeCardTapped(badgeKind: BadgeKind) {
        onNewMessage(ProfileFeatureMessageBadgeClicked(badgeKind: badgeKind))
    }

    func doBadgesVisibilityButtonTapped(visibilityButton: ProfileFeatureMessageBadgesVisibilityButton) {
        onNewMessage(ProfileFeatureMessageBadgesVisibilityButtonClicked(visibilityButton: visibilityButton))
    }

    // MARK: Daily study reminders

    func setDailyStudyRemindersEnabled(_ isEnabled: Bool) {
        onNewMessage(ProfileFeatureMessageDailyStudyRemindersToggleClicked(isEnabled: isEnabled))

        guard isEnabled else {
            return notificationService.removeDailyStudyReminderLocalNotifications()
        }

        Task(priority: .userInitiated) {
            await notificationsRegistrationService.requestAuthorizationIfNeeded()
        }
    }

    func setDailyStudyRemindersStartHour(_ startHour: Int) {
        onNewMessage(
            ProfileFeatureMessageDailyStudyRemindersIntervalStartHourChanged(startHour: Int32(startHour))
        )
    }

    func determineCurrentNotificationPermissionStatus() {
        Task {
            currentNotificationPermissionStatus = await NotificationPermissionStatus.current
        }
    }

    private func handleCurrentNotificationPermissionStatusDidSet(
        oldValue: NotificationPermissionStatus?,
        newValue: NotificationPermissionStatus?
    ) {
        guard oldValue != newValue,
              case .content(let content) = stateKs,
              content.dailyStudyRemindersState.isEnabled else {
            return
        }

        DispatchQueue.main.async {
            self.objectWillChange.send()
        }
    }

    @objc
    private func onPermissionStatusUpdate(_ notification: Foundation.Notification) {
        if let notificationPermissionStatus = notification.object as? NotificationPermissionStatus {
            currentNotificationPermissionStatus = notificationPermissionStatus
        }
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(ProfileFeatureMessageViewedEventMessage())
    }

    func logClickedSettingsEvent() {
        onNewMessage(ProfileFeatureMessageClickedSettingsEventMessage())
    }

    func logClickedDailyStudyRemindsTimeEvent() {
        onNewMessage(ProfileFeatureMessageClickedDailyStudyRemindsTimeEventMessage())
    }

    func logStreakFreezeModalShownEvent() {
        onNewMessage(ProfileFeatureMessageStreakFreezeModalShownEventMessage())
    }

    func logStreakFreezeModalHiddenEvent() {
        onNewMessage(ProfileFeatureMessageStreakFreezeModalHiddenEventMessage())
    }
}

// MARK: - ProfileViewModel: BadgeDetailsModalViewControllerDelegate -

extension ProfileViewModel: BadgeDetailsModalViewControllerDelegate {
    func badgeDetailsModalViewControllerDidAppear(
        _ viewController: BadgeDetailsModalViewController,
        badgeKind: BadgeKind
    ) {
        onNewMessage(ProfileFeatureMessageBadgeModalShownEventMessage(badgeKind: badgeKind))
    }

    func badgeDetailsModalViewControllerDidDisappear(
        _ viewController: BadgeDetailsModalViewController,
        badgeKind: BadgeKind
    ) {
        onNewMessage(ProfileFeatureMessageBadgeModalHiddenEventMessage(badgeKind: badgeKind))
    }
}
