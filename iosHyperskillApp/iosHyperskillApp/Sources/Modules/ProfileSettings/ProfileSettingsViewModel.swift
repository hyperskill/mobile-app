import Foundation
import shared

final class ProfileSettingsViewModel: FeatureViewModel<
  ProfileSettingsFeatureViewState,
  ProfileSettingsFeatureMessage,
  ProfileSettingsFeatureActionViewAction
> {
    private static let applyNewThemeAnimationDelay: TimeInterval = 0.33
    private static let dismissScreenAnimationDelay =
      AppRouter.Animation.swapRootViewControllerAnimationDuration * 0.75

    private let applicationThemeService: ApplicationThemeServiceProtocol

    private lazy var sendEmailFeedbackController = SendEmailFeedbackController()

    // It's impossible to handle onTap on `Picker`, so using `onAppear` callback with debouncer.
    private let analyticLogClickedThemeEventDebouncer: DebouncerProtocol = Debouncer()

    var stateKs: ProfileSettingsFeatureViewStateKs { .init(state) }

    init(
        applicationThemeService: ApplicationThemeServiceProtocol,
        feature: Presentation_reduxFeature
    ) {
        self.applicationThemeService = applicationThemeService
        super.init(feature: feature)

        onNewMessage(ProfileSettingsFeatureMessageInitMessage())
    }

    override func shouldNotifyStateDidChange(
        oldState: ProfileSettingsFeatureViewState,
        newState: ProfileSettingsFeatureViewState
    ) -> Bool {
        ProfileSettingsFeatureViewStateKs(oldState) != ProfileSettingsFeatureViewStateKs(newState)
    }

    func doThemeChange(newTheme: ApplicationTheme) {
        onNewMessage(ProfileSettingsFeatureMessageThemeChanged(theme: newTheme.sharedTheme))
        DispatchQueue.main.asyncAfter(deadline: .now() + Self.applyNewThemeAnimationDelay) {
            self.applicationThemeService.apply(newTheme: newTheme)
        }
    }

    func doSignOut() {
        WebCacheCleaner.clean {
            self.onNewMessage(ProfileSettingsFeatureMessageSignOutConfirmed())

            DispatchQueue.main.asyncAfter(deadline: .now() + Self.dismissScreenAnimationDelay) {
                self.onNewMessage(ProfileSettingsFeatureMessageDismissScreen())
            }
        }
    }

    func doSendFeedback() {
        onNewMessage(ProfileSettingsFeatureMessageClickedSendFeedback())
    }

    func doSendFeedbackPresentation(feedbackEmailData: FeedbackEmailData) {
        guard let currentPresentedViewController = SourcelessRouter().currentPresentedViewController() else {
            return
        }

        sendEmailFeedbackController.sendFeedback(
            feedbackEmailData: feedbackEmailData,
            presentationController: currentPresentedViewController
        )
    }

    func doDeleteAccount(isConfirmed: Bool) {
        onNewMessage(ProfileSettingsFeatureMessageDeleteAccountNoticeHidden(isConfirmed: isConfirmed))
    }

    func doRateInAppStorePresentation() {
        onNewMessage(ProfileSettingsFeatureMessageClickedRateUsInAppStoreEventMessage())

        guard let url = URL(string: Strings.Settings.rateInAppStoreURL) else {
            return assertionFailure("Invalid URL")
        }

        UIApplication.shared.open(url, options: [:]) { success in
            if !success {
                WebControllerManager.shared.presentWebControllerWithURL(
                    url,
                    controllerType: .safari
                )
            }
        }
    }

    func doSubscriptionDetailsPresentation() {
        onNewMessage(ProfileSettingsFeatureMessageSubscriptionDetailsClicked())
    }

    // MARK: Analytic

    func logViewedEvent() {
        onNewMessage(ProfileSettingsFeatureMessageViewedEventMessage())
    }

    func logClickedDoneEvent() {
        onNewMessage(ProfileSettingsFeatureMessageClickedDoneEventMessage())
    }

    func logClickedThemeEvent() {
        analyticLogClickedThemeEventDebouncer.action = sendClickedThemeEventMessage
    }

    private func sendClickedThemeEventMessage() {
        onNewMessage(ProfileSettingsFeatureMessageClickedThemeEventMessage())
    }

    func logClickedTermsOfServiceEvent() {
        onNewMessage(ProfileSettingsFeatureMessageClickedTermsOfServiceEventMessage())
    }

    func logClickedPrivacyPolicyEvent() {
        onNewMessage(ProfileSettingsFeatureMessageClickedPrivacyPolicyEventMessage())
    }

    func logClickedReportProblemEvent() {
        onNewMessage(ProfileSettingsFeatureMessageClickedReportProblemEventMessage())
    }

    func logClickedSignOutEvent() {
        onNewMessage(ProfileSettingsFeatureMessageClickedSignOutEventMessage())
    }

    func logSignOutNoticeShownEvent() {
        onNewMessage(ProfileSettingsFeatureMessageSignOutNoticeShownEventMessage())
    }

    func logSignOutNoticeHiddenEvent(isConfirmed: Bool) {
        onNewMessage(
            ProfileSettingsFeatureMessageSignOutNoticeHiddenEventMessage(isConfirmed: isConfirmed)
        )
    }

    func logClickedDeleteAccountEvent() {
        onNewMessage(ProfileSettingsFeatureMessageClickedDeleteAccountEventMessage())
    }

    func logDeleteAccountNoticeShownEvent() {
        onNewMessage(ProfileSettingsFeatureMessageDeleteAccountNoticeShownEventMessage())
    }
}
