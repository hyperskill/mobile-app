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
    @ObservedObject private var viewModel: AuthViewModel

    let appearance: Appearance

    init(viewModel: AuthViewModel, appearance: Appearance = Appearance()) {
        self.viewModel = viewModel
        self.appearance = appearance
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        ZStack {
            Color(SharedResources.colors.shared.color_background.dynamicUIColor).ignoresSafeArea()
            
            
            VStack {
                HStack {
                    Image("close_icon").frame(width: 16, height: 16).padding(.horizontal, 16)
                    Spacer()
                }
                Spacer()
                VStack {
                    Image("logo")
                        .resizable()
                        .frame(width: appearance.logoSize, height: appearance.logoSize)
                        .padding(.top)
                    HStack {
                        Text(SharedResources.strings.shared.auth_log_in_title.localized())
                            .font(.body).multilineTextAlignment(.center)
                    }
                }
                Spacer()
                VStack(alignment: .center, spacing: 8) {
                    AuthSocialButton(viewModel: viewModel, provider: .jetbrains)
                    AuthSocialButton(viewModel: viewModel, provider: .google)
                    AuthSocialButton(viewModel: viewModel, provider: .github)
                    AuthSocialButton(viewModel: viewModel, provider: .apple)
                    Button(
                        action: {},
                        label: {
                            HStack{
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
                }
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
