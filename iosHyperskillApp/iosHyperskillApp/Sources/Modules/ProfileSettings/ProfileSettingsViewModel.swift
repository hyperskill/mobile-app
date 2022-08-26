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
        onNewMessage(ProfileSettingsFeatureMessageProfileSettingsViewedEventMessage())
    }

    func logClickedDoneEvent() {
        onNewMessage(
            ProfileSettingsFeatureMessageProfileSettingsClickedEventMessage(
                part: HyperskillAnalyticPart.head,
                target: HyperskillAnalyticTarget.done
            )
        )
    }

    func logClickedThemeEvent() {
        analyticLogClickedThemeEventDebouncer.action = sendClickedThemeEventMessage
    }

    func sendClickedThemeEventMessage() {
        onNewMessage(
            ProfileSettingsFeatureMessageProfileSettingsClickedEventMessage(
                part: HyperskillAnalyticPart.main,
                target: HyperskillAnalyticTarget.theme
            )
        )
    }

    func logClickedTermsOfServiceEvent() {
        onNewMessage(
            ProfileSettingsFeatureMessageProfileSettingsClickedEventMessage(
                part: HyperskillAnalyticPart.main,
                target: HyperskillAnalyticTarget.jetbrainsTermsOfService
            )
        )
    }

    func logClickedPrivacyPolicyEvent() {
        onNewMessage(
            ProfileSettingsFeatureMessageProfileSettingsClickedEventMessage(
                part: HyperskillAnalyticPart.main,
                target: HyperskillAnalyticTarget.hyperskillTermsOfService
            )
        )
    }

    func logClickedHelpCenterEvent() {
        onNewMessage(
            ProfileSettingsFeatureMessageProfileSettingsClickedEventMessage(
                part: HyperskillAnalyticPart.main,
                target: HyperskillAnalyticTarget.helpCenter
            )
        )
    }

    func logClickedLogoutEvent() {
        onNewMessage(
            ProfileSettingsFeatureMessageProfileSettingsClickedEventMessage(
                part: HyperskillAnalyticPart.main,
                target: HyperskillAnalyticTarget.logout
            )
        )
    }

    func logClickedDeleteAccountEvent() {
        onNewMessage(
            ProfileSettingsFeatureMessageProfileSettingsClickedEventMessage(
                part: HyperskillAnalyticPart.main,
                target: HyperskillAnalyticTarget.deleteAccount
            )
        )
    }
}
