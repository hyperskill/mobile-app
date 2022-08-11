import shared
import SwiftUI

struct ProfileSettingsView: View {
    private static let termsOfServiceURL = URL(string: "https://www.jetbrains.com/legal/terms/jetbrains-academy.html")
    private static let privacyPolicyURL = URL(string: "https://hi.hyperskill.org/terms")
    private static let helpCenterURL = URL(string: "https://support.hyperskill.org/hc/en-us")

    @StateObject var viewModel: ProfileSettingsViewModel

    @State private var isPresentingLogoutAlert = false

    @Environment(\.presentationMode) private var presentationMode

    var body: some View {
        NavigationView {
            buildBody()
                .navigationBarTitleDisplayMode(.inline)
                .navigationTitle(Strings.Settings.title)
                .toolbar {
                    ToolbarItem(placement: .primaryAction) {
                        Button(Strings.General.done) {
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
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.state {
        case is ProfileSettingsFeatureStateIdle:
            ProgressView()
                .onAppear {
                    viewModel.loadProfileSettings()
                }
        case is ProfileSettingsFeatureStateLoading:
            ProgressView()
        case is ProfileSettingsFeatureStateError:
            PlaceholderView(
                configuration: .networkError {
                    viewModel.loadProfileSettings(forceUpdate: true)
                }
            )
        case let content as ProfileSettingsFeatureStateContent:
            buildContent(profileSettings: content.profileSettings)
        default:
            Text("Unkwown state")
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
                        } else {
                            Text(theme.title)
                        }
                    }
                }
            }

            Section(header: Text(Strings.Settings.about)) {
                if let termsOfServiceURL = Self.termsOfServiceURL {
                    OpenURLInsideAppButton(
                        text: Strings.Settings.termsOfService,
                        url: termsOfServiceURL
                    )
                    .foregroundColor(.primaryText)
                }

                if let privacyPolicyURL = Self.privacyPolicyURL {
                    OpenURLInsideAppButton(
                        text: Strings.Settings.privacyPolicy,
                        url: privacyPolicyURL
                    )
                    .foregroundColor(.primaryText)
                }

                if let helpCenterURL = Self.helpCenterURL {
                    OpenURLInsideAppButton(
                        text: Strings.Settings.helpCenter,
                        url: helpCenterURL
                    )
                    .foregroundColor(.primaryText)
                }

                HStack {
                    Text(Strings.Settings.version)
                        .foregroundColor(.primaryText)

                    Spacer()

                    if let shortVersionWithBuildNumberString = MainBundleInfo.shortVersionWithBuildNumberString {
                        Text(shortVersionWithBuildNumberString)
                            .foregroundColor(.secondaryText)
                    }
                }

                Button(Strings.Settings.rateApplication) {
                }
                .foregroundColor(Color(ColorPalette.primary))
            }

            Section {
                Button(Strings.Settings.logout) {
                    isPresentingLogoutAlert = true
                }
                .foregroundColor(Color(ColorPalette.overlayRed))
                .alert(isPresented: $isPresentingLogoutAlert) {
                    Alert(
                        title: Text(Strings.Settings.logoutDialogTitle),
                        message: Text(Strings.Settings.logoutDialogExplanation),
                        primaryButton: .default(Text(Strings.General.no)),
                        secondaryButton: .destructive(
                            Text(Strings.General.yes),
                            action: viewModel.doLogout
                        )
                    )
                }
            }

            Section {
                Button(Strings.Settings.deleteAccount) {
                }
                .foregroundColor(Color(ColorPalette.overlayRed))
            }
        }
    }

    private func handleViewAction(_ viewAction: ProfileSettingsFeatureActionViewAction) {
        print("ProfileSettingsView :: unhandled viewAction = \(viewAction)")
    }
}

struct ProfileSettingsView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileSettingsAssembly().makeModule()
    }
}
