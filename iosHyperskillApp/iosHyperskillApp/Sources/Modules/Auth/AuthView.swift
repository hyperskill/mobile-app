import shared
import SwiftUI

extension AuthView {
    struct Appearance {
        let logoSize: CGFloat = 48

        let googleButtonForegroundColor = Color.purple
        let googleButtonMinHeight: CGFloat = 44
        let googleButtonOverlayCornerRadius: CGFloat = 8
        let googleButtonOverlayStrokeColor = Color.purple
        let googleButtonOverlayStrokeWidth: CGFloat = 2
    }
}

struct AuthView: View {
    @Environment(\.horizontalSizeClass) var horizontalSizeClass
    @Environment(\.verticalSizeClass) var verticalSizeClass

    @ObservedObject private var viewModel: AuthViewModel

    let appearance: Appearance

    init(viewModel: AuthViewModel, appearance: Appearance = Appearance()) {
        self.viewModel = viewModel
        self.appearance = appearance
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        ZStack {
            Color(ColorPalette.background).ignoresSafeArea()

            VStack {
                if horizontalSizeClass == .regular && verticalSizeClass == .regular {
                    Spacer()
                }
                VStack {
                    Image("logo")
                        .resizable()
                        .frame(width: appearance.logoSize, height: appearance.logoSize)

                    HStack {
                        Text(SharedResources.strings.shared.auth_log_in_title.localized())
                            .font(.body).multilineTextAlignment(.center)
                    }
                }

                VStack(alignment: .center, spacing: 8) {
                    ForEach(viewModel.availableSocialAuthProviders, id: \.rawValue) { provider in
                        AuthSocialButton(
                            text: provider.text,
                            imageName: provider.imageName,
                            action: viewModel.signInWithSocialProvider(provider: provider)
                        ).padding(.horizontal, horizontalSizeClass == .regular ? 110 : 20)
                    }

                    Button(
                        action: {},
                        label: {
                            HStack {
                                Text("Continue with email")
                                    .font(.subheadline)
                                    .frame(maxWidth: .infinity, minHeight: 48, alignment: .center)
                                    .foregroundColor(Color(SharedResources.colors.shared.color_primary.dynamicUIColor))
                            }.overlay(
                                RoundedRectangle(
                                    cornerRadius: 8
                                )
                                .stroke(
                                    Color(SharedResources.colors.shared.color_primary_alpha_50.dynamicUIColor),
                                    lineWidth: 1
                                )
                            )
                        }
                    ).padding(.horizontal, horizontalSizeClass == .regular ? 110 : 20)
                        .padding(.top, 16)
                }.padding(.top, 48)
                Spacer()
            }.padding(.top, horizontalSizeClass == .regular && verticalSizeClass == .regular ? 0 : 44)
        }
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: AuthFeatureActionViewAction) {
        print("AuthView :: \(#function) viewAction = \(viewAction)")
    }
}

fileprivate extension SocialProvider {
    var text: String {
        switch self {
        case .jetbrains:
            return "JetBrains Account"
        case .google:
            return "Google"
        case .github:
            return "GitHub"
        case .apple:
            return "Apple"
        }
    }
    var imageName: String {
        switch self {
        case .jetbrains:
            return "jetbrains_logo"
        case .google:
            return "google_logo"
        case .github:
            return "github_logo"
        case .apple:
            return "apple_logo"
        }
    }
}

struct AuthView_Previews: PreviewProvider {
    static var previews: some View {
        AuthAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))
        AuthAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))
        AuthAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))

    }
}
