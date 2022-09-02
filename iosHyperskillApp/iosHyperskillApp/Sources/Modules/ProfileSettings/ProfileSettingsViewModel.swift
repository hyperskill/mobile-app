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
}
