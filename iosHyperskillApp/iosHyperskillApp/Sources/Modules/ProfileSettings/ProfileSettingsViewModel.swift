import Foundation
import shared

final class ProfileSettingsViewModel: FeatureViewModel<
  ProfileSettingsFeatureState,
  ProfileSettingsFeatureMessage,
  ProfileSettingsFeatureActionViewAction
> {
    private static let applyNewThemeAnimationDelay: TimeInterval = 0.33

    private let applicationThemeService: ApplicationThemeServiceProtocol

    private lazy var sendEmailFeedbackController = SendEmailFeedbackController()

    // It's impossible to handle onTap on `Picker`, so using `onAppear` callback with debouncer.
    private let analyticLogClickedThemeEventDebouncer: DebouncerProtocol = Debouncer()

    init(
        applicationThemeService: ApplicationThemeServiceProtocol,
        feature: Presentation_reduxFeature
    ) {
        self.applicationThemeService = applicationThemeService
        super.init(feature: feature)
    }

    func loadProfileSettings(forceUpdate: Bool = false) {
        onNewMessage(ProfileSettingsFeatureMessageInitMessage(forceUpdate: forceUpdate))
    }

    func doThemeChange(newTheme: ApplicationTheme) {
        onNewMessage(ProfileSettingsFeatureMessageThemeChanged(theme: newTheme.sharedTheme))
        DispatchQueue.main.asyncAfter(deadline: .now() + Self.applyNewThemeAnimationDelay) {
            self.applicationThemeService.apply(newTheme: newTheme)
        }
    }

    func doLogout() {
        onNewMessage(ProfileSettingsFeatureMessageLogoutConfirmed())
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

    func logClickedLogoutEvent() {
        onNewMessage(ProfileSettingsFeatureMessageClickedLogoutEventMessage())
    }

    func logLogoutNoticeShownEvent() {
        onNewMessage(ProfileSettingsFeatureMessageLogoutNoticeShownEventMessage())
    }

    func logLogoutNoticeHiddenEvent(isConfirmed: Bool) {
        onNewMessage(
            ProfileSettingsFeatureMessageLogoutNoticeHiddenEventMessage(isConfirmed: isConfirmed)
        )
    }

    func logClickedDeleteAccountEvent() {
        onNewMessage(ProfileSettingsFeatureMessageClickedDeleteAccountEventMessage())
    }

    func logDeleteAccountNoticeShownEvent() {
        onNewMessage(ProfileSettingsFeatureMessageDeleteAccountNoticeShownEventMessage())
    }

    func logDeleteAccountNoticeHiddenEvent(isConfirmed: Bool) {
        onNewMessage(
            ProfileSettingsFeatureMessageDeleteAccountNoticeHiddenEventMessage(isConfirmed: isConfirmed)
        )
    }
}
