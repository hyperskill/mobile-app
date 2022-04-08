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

struct AuthView: View {
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
                VStack {
                    Image("logo")
                        .resizable()
                        .frame(width: appearance.logoSize, height: appearance.logoSize)

                    HStack {
                        Text(SharedResources.strings.shared.auth_log_in_title.localized())
                            .font(.body).multilineTextAlignment(.center)
                    }
                }.padding(.top, 44)

                VStack(alignment: .center, spacing: 8) {
                    ForEach(viewModel.availableSocialAuthProviders, id: \.rawValue) { provider in
                        AuthSocialButton(
                            text: provider.text,
                            imageName: provider.imageName,
                            action: viewModel.signInWithSocialProvider(provider: provider)
                        )
                    }

                    Button(
                        action: {},
                        label: {
                            HStack {
                                Spacer()
                                Text("Continue with email")
                                    .font(.subheadline)
                                    .foregroundColor(Color(SharedResources.colors.shared.color_primary.dynamicUIColor))
                                    .padding()
                                Spacer()
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
                    ).padding(.horizontal, 20)
                        .padding(.top, 16)
                }.padding(.top, 48)
                Spacer()
            }
        }
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: AuthFeatureActionViewAction) {
        print("AuthView :: \(#function) viewAction = \(viewAction)")
    }
}

struct AuthView_Previews: PreviewProvider {
    static var previews: some View {
        AuthAssembly().makeModule()
    }
}
