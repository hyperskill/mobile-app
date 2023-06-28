import Foundation
import shared

final class ProfileSettingsViewModel: FeatureViewModel<
  ProfileSettingsFeatureState,
  ProfileSettingsFeatureMessage,
  ProfileSettingsFeatureActionViewAction
> {
    private static let applyNewThemeAnimationDelay: TimeInterval = 0.33
    private static let dismissScreenAnimationDelay =
      AppViewController.Animation.swapRootViewControllerAnimationDuration * 0.75

    private let applicationThemeService: ApplicationThemeServiceProtocol

    private lazy var sendEmailFeedbackController = SendEmailFeedbackController()

    // It's impossible to handle onTap on `Picker`, so using `onAppear` callback with debouncer.
    private let analyticLogClickedThemeEventDebouncer: DebouncerProtocol = Debouncer()

    var stateKs: ProfileSettingsFeatureStateKs { .init(state) }

    init(
        applicationThemeService: ApplicationThemeServiceProtocol,
        feature: Presentation_reduxFeature
    ) {
        self.applicationThemeService = applicationThemeService
        super.init(feature: feature)

        onNewMessage(ProfileSettingsFeatureMessageInitMessage(forceUpdate: false))
    }

    override func shouldNotifyStateDidChange(
        oldState: ProfileSettingsFeatureState,
        newState: ProfileSettingsFeatureState
    ) -> Bool {
        ProfileSettingsFeatureStateKs(oldState) != ProfileSettingsFeatureStateKs(newState)
    }

    func doRetryLoadProfileSettings() {
        onNewMessage(ProfileSettingsFeatureMessageInitMessage(forceUpdate: true))
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
