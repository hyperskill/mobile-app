import Foundation
import shared

final class ProfileSettingsViewModel: FeatureViewModel<
  ProfileSettingsFeatureState,
  ProfileSettingsFeatureMessage,
  ProfileSettingsFeatureActionViewAction
> {
    private static let applyNewThemeAnimationDelay: TimeInterval = 0.33

    private let applicationThemeService: ApplicationThemeServiceProtocol

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

    func logClickedHelpCenterEvent() {
        onNewMessage(ProfileSettingsFeatureMessageClickedHelpCenterEventMessage())
    }

    func logClickedLogoutEvent() {
        onNewMessage(ProfileSettingsFeatureMessageClickedLogoutEventMessage())
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
