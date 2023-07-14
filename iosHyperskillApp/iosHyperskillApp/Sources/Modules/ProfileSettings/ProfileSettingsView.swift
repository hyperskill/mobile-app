import shared
import SwiftUI

struct ProfileSettingsView: View {
    private static let termsOfServiceURL = URL(string: Strings.Settings.termsOfServiceURL).require()
    private static let privacyPolicyURL = URL(string: Strings.Settings.privacyPolicyURL).require()
    private static let reportProblemURL = URL(string: Strings.Settings.reportProblemURL).require()

    @StateObject var viewModel: ProfileSettingsViewModel

    @State private var isPresentingSignOutAlert = false
    @State private var isPresentingAccountDeletionAlert = false

    @Environment(\.presentationMode) private var presentationMode

    var body: some View {
        NavigationView {
            ZStack {
                UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

                buildBody()
            }
            .navigationBarTitleDisplayMode(.inline)
            .navigationTitle(Strings.Settings.title)
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    Button(Strings.Common.done) {
                        viewModel.logClickedDoneEvent()
                        presentationMode.wrappedValue.dismiss()
                    }
                }
            }
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear {
            viewModel.stopListening()
            viewModel.onViewAction = nil
        }
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.stateKs {
        case .idle, .loading:
            ProgressView()
        case .error:
            PlaceholderView(configuration: .networkError(action: viewModel.doRetryLoadProfileSettings))
        case .content(let content):
            if content.isLoadingMagicLink {
                let _ = ProgressHUD.show()
            }

            buildContent(profileSettings: content.profileSettings)
        }
    }

    @ViewBuilder
    private func buildContent(profileSettings: ProfileSettings) -> some View {
        Form {
            Section(header: Text(Strings.Settings.appearance)) {
                Picker(
                    Strings.Settings.Theme.title,
                    selection: Binding<ApplicationTheme>(
                        get: { ApplicationTheme(sharedTheme: profileSettings.theme) },
                        set: { viewModel.doThemeChange(newTheme: $0) }
                    )
                ) {
                    ForEach(ApplicationTheme.allCases) { theme in
                        if theme != ApplicationTheme(sharedTheme: profileSettings.theme) {
                            Text(theme.title)
                                .navigationTitle(Strings.Settings.Theme.title)
                                .onAppear(perform: viewModel.logClickedThemeEvent)
                        } else {
                            Text(theme.title)
                        }
                    }
                }
            }

            Section(header: Text(Strings.Settings.about)) {
                OpenURLInsideAppButton(
                    text: Strings.Settings.termsOfService,
                    url: Self.termsOfServiceURL,
                    webControllerType: .inAppSafari,
                    onTap: viewModel.logClickedTermsOfServiceEvent
                )
                .foregroundColor(.primaryText)

                OpenURLInsideAppButton(
                    text: Strings.Settings.privacyPolicy,
                    url: Self.privacyPolicyURL,
                    webControllerType: .inAppSafari,
                    onTap: viewModel.logClickedPrivacyPolicyEvent
                )
                .foregroundColor(.primaryText)

                HStack {
                    Text(Strings.Settings.version)
                        .foregroundColor(.primaryText)

                    Spacer()

                    if let shortVersionWithBuildNumberString = MainBundleInfo.shortVersionWithBuildNumberString {
                        Text(shortVersionWithBuildNumberString)
                            .foregroundColor(.secondaryText)
                    }
                }

                // ALTAPPS-312
                //Button(Strings.Settings.rateApplication) {
                //}
                //.foregroundColor(Color(ColorPalette.primary))
            }

            Section {
                Button(Strings.Settings.sendFeedback, action: viewModel.doSendFeedback)
                    .foregroundColor(.primaryText)

                OpenURLInsideAppButton(
                    text: Strings.Settings.reportProblem,
                    url: Self.reportProblemURL,
                    webControllerType: .inAppSafari,
                    onTap: viewModel.logClickedReportProblemEvent
                )
                .foregroundColor(.primaryText)
            }

            Section {
                Button(Strings.Settings.signOut) {
                    viewModel.logClickedSignOutEvent()
                    isPresentingSignOutAlert = true
                }
                .foregroundColor(Color(ColorPalette.overlayRed))
                .alert(isPresented: $isPresentingSignOutAlert) {
                    Alert(
                        title: Text(Strings.Settings.signOutAlertTitle),
                        message: Text(Strings.Settings.signOutAlertMessage),
                        primaryButton: .default(
                            Text(Strings.Common.no),
                            action: {
                                viewModel.logSignOutNoticeHiddenEvent(isConfirmed: false)
                            }
                        ),
                        secondaryButton: .destructive(
                            Text(Strings.Common.yes),
                            action: {
                                viewModel.logSignOutNoticeHiddenEvent(isConfirmed: true)
                                viewModel.doSignOut()
                            }
                        )
                    )
                }
                .onChange(of: isPresentingSignOutAlert) { newValue in
                    if newValue {
                        viewModel.logSignOutNoticeShownEvent()
                    }
                }
            }

            Section {
                Button(Strings.Settings.deleteAccount) {
                    viewModel.logClickedDeleteAccountEvent()
                    isPresentingAccountDeletionAlert = true
                }
                .foregroundColor(Color(ColorPalette.overlayRed))
                .alert(isPresented: $isPresentingAccountDeletionAlert) {
                    Alert(
                        title: Text(Strings.Settings.deleteAccountAlertTitle),
                        message: Text(Strings.Settings.deleteAccountAlertMessage),
                        primaryButton: .default(
                            Text(Strings.Common.cancel),
                            action: {
                                viewModel.doDeleteAccount(isConfirmed: false)
                            }
                        ),
                        secondaryButton: .destructive(
                            Text(Strings.Settings.deleteAccountAlertDeleteButton),
                            action: {
                                viewModel.doDeleteAccount(isConfirmed: true)
                            }
                        )
                    )
                }
                .onChange(of: isPresentingAccountDeletionAlert) { newValue in
                    if newValue {
                        viewModel.logDeleteAccountNoticeShownEvent()
                    }
                }
            }
        }
    }

    private func handleViewAction(_ viewAction: ProfileSettingsFeatureActionViewAction) {
        switch ProfileSettingsFeatureActionViewActionKs(viewAction) {
        case .sendFeedback(let sendFeedbackViewAction):
            viewModel.doSendFeedbackPresentation(feedbackEmailData: sendFeedbackViewAction.feedbackEmailData)
        case .openUrl(let data):
            ProgressHUD.showSuccess()
            WebControllerManager.shared.presentWebControllerWithURLString(data.url, controllerType: .inAppCustom())
        case .showGetMagicLinkError:
            ProgressHUD.showError()
        case .navigateTo(let navigateToViewAction):
            switch ProfileSettingsFeatureActionViewActionNavigateToKs(navigateToViewAction) {
            case .parentScreen:
                presentationMode.wrappedValue.dismiss()
            }
        }
    }
}

struct ProfileSettingsView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileSettingsAssembly().makeModule()
    }
}
