import shared
import SwiftUI

struct ProfileSettingsView: View {
    private static let termsOfServiceURL = URL(string: Strings.Settings.termsOfServiceURL).require()
    private static let privacyPolicyURL = URL(string: Strings.Settings.privacyPolicyURL).require()
    private static let reportProblemURL = URL(string: Strings.Settings.reportProblemURL).require()
    private static let accountDeletionURL = URL(string: Strings.Settings.accountDeletionURL).require()

    @StateObject var viewModel: ProfileSettingsViewModel

    @State private var isPresentingSignOutAlert = false
    @State private var isPresentingAccountDeletionAlert = false

    @Environment(\.presentationMode) private var presentationMode

    var body: some View {
        NavigationView {
            ZStack {
                UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

                //buildBody()
            }
            .navigationBarTitleDisplayMode(.inline)
            .navigationTitle(Strings.Settings.title)
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    Button(Strings.General.done) {
                        viewModel.logClickedDoneEvent()
                        presentationMode.wrappedValue.dismiss()
                    }
                }
            }
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear(perform: viewModel.stopListening)
        .navigationViewStyle(StackNavigationViewStyle())
    }

    // MARK: Private API

//    @ViewBuilder
//    private func buildBody() -> some View {
//        switch viewModel.state {
//        case is ProfileSettingsFeatureStateIdle:
//            ProgressView()
//                .onAppear {
//                    viewModel.loadProfileSettings()
//                }
//        case is ProfileSettingsFeatureStateLoading:
//            ProgressView()
//        case is ProfileSettingsFeatureStateError:
//            PlaceholderView(
//                configuration: .networkError {
//                    viewModel.loadProfileSettings(forceUpdate: true)
//                }
//            )
//        case let content as ProfileSettingsFeatureStateContent:
//            buildContent(profileSettings: content.profileSettings)
//        default:
//            Text("Unkwown state")
//        }
//    }

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
                    webControllerType: .safari,
                    onTap: viewModel.logClickedTermsOfServiceEvent
                )
                .foregroundColor(.primaryText)

                OpenURLInsideAppButton(
                    text: Strings.Settings.privacyPolicy,
                    url: Self.privacyPolicyURL,
                    webControllerType: .safari,
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
                    webControllerType: .safari,
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
                            Text(Strings.General.no),
                            action: {
                                viewModel.logSignOutNoticeHiddenEvent(isConfirmed: false)
                            }
                        ),
                        secondaryButton: .destructive(
                            Text(Strings.General.yes),
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
                            Text(Strings.General.cancel),
                            action: {
                                viewModel.logDeleteAccountNoticeHiddenEvent(isConfirmed: false)
                            }
                        ),
                        secondaryButton: .destructive(
                            Text(Strings.Settings.deleteAccountAlertDeleteButton),
                            action: {
                                viewModel.logDeleteAccountNoticeHiddenEvent(isConfirmed: true)
                                WebControllerManager.shared.presentWebControllerWithURL(
                                    Self.accountDeletionURL,
                                    withKey: .externalLink,
                                    controllerType: .custom()
                                )
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
        switch viewAction {
        case is ProfileSettingsFeatureActionViewActionNavigateToParentScreen:
            presentationMode.wrappedValue.dismiss()
        case let sendFeedbackViewAction as ProfileSettingsFeatureActionViewActionSendFeedback:
            viewModel.doSendFeedbackPresentation(feedbackEmailData: sendFeedbackViewAction.feedbackEmailData)
        default:
            print("ProfileSettingsView :: unhandled viewAction = \(viewAction)")
        }
    }
}

struct ProfileSettingsView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileSettingsAssembly().makeModule()
    }
}
