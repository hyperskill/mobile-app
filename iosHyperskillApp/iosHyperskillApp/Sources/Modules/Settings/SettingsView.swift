import shared
import SwiftUI

enum Theme: String, CaseIterable, Identifiable {
    case light, dark, system
    var id: Self { self }
}


struct SettingsView: View {
    @Environment(\.presentationMode) private var presentationMode

    private static let termsOfServiceURL = HyperskillURLFactory.makeTermOfService()

    private static let privacyPolicyURL = HyperskillURLFactory.makePrivacyPolicy()

    private static let helpCenterURL = HyperskillURLFactory.makeHelpCenter()

    @State private var selectedTheme: Theme = .light

    var body: some View {
        NavigationView {
            Form {
                Section(
                    header: Text(Strings.Settings.appearance)
                        .font(.headline)
                        .foregroundColor(Color(ColorPalette.primary))
                ) {
                    Picker(Strings.Settings.theme, selection: $selectedTheme) {
                        Text(Strings.Settings.light).tag(Theme.light)
                        Text(Strings.Settings.dark).tag(Theme.dark)
                        Text(Strings.Settings.system).tag(Theme.system)
                    }
                }

                Section(
                    header: Text(Strings.Settings.about)
                        .font(.headline)
                        .foregroundColor(Color(ColorPalette.primary))
                ) {
                    if let termsOfServiceURL = Self.termsOfServiceURL {
                        URLButton(
                            text: Strings.Settings.termsOfService,
                            url: termsOfServiceURL
                        )
                        .foregroundColor(.primaryText)
                    }

                    if let privacyPolicyURL = Self.privacyPolicyURL {
                        URLButton(
                            text: Strings.Settings.privacyPolicy,
                            url: privacyPolicyURL
                        )
                        .foregroundColor(.primaryText)
                    }

                    if let helpCenterURL = Self.helpCenterURL {
                        URLButton(
                            text: Strings.Settings.helpCenter,
                            url: helpCenterURL
                        )
                        .foregroundColor(.primaryText)
                    }

                    HStack {
                        Text(Strings.Settings.version)
                            .foregroundColor(.primaryText)
                            .font(.body)
                        Spacer()
                        if let version = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String {
                            Text(version)
                                .foregroundColor(.secondaryText)
                                .font(.body)
                        }
                    }

                    Button(Strings.Settings.rateApplication) {
                    }
                    .foregroundColor(Color(ColorPalette.primary))
                    .font(.body)
                }

                Section {
                    Button(Strings.Settings.logout) {
                    }
                    .foregroundColor(Color(ColorPalette.overlayRed))
                }

                Section {
                    Button(Strings.Settings.deleteAccount) {
                    }
                    .foregroundColor(Color(ColorPalette.overlayRed))
                }
            }



            .navigationBarTitleDisplayMode(.inline)
            .navigationTitle(Strings.Settings.title)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(
                        action: { presentationMode.wrappedValue.dismiss() },
                        label: {
                            Text(Strings.Settings.done)
                                .font(.body)
                                .foregroundColor(Color(ColorPalette.primary))
                        }
                    )
                }
            }
        }
    }
}

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsView()
    }
}
