import shared
import SwiftUI

extension AuthSocialView {
    struct Appearance {
        let logoWidthHeight: CGFloat = 48
    }
}

struct AuthSocialView: View {
    private(set) var appearance = Appearance()

    let isInSignUpMode: Bool

    @StateObject var viewModel: AuthSocialViewModel

    @State private var presentingAuthWithEmail = false

    var body: some View {
        let _ = renderProgressHUD(state: viewModel.state)

        return NavigationView {
            AuthAdaptiveContentView(
                onViewDidAppear: viewModel.logViewedEvent
            ) { horizontalSizeClass in
                AuthLogoView(
                    logoWidthHeight: appearance.logoWidthHeight,
                    title: isInSignUpMode ? Strings.Auth.Social.signUpTitle : Strings.Auth.Social.logInTitle
                )
                .padding(horizontalSizeClass == .regular ? .bottom : .vertical, appearance.logoWidthHeight)

                AuthSocialControlsView(
                    socialAuthProviders: viewModel.availableSocialAuthProviders,
                    errorMessage: viewModel.authSocialErrorMessage,
                    isContinueWithEmailAvailable: !isInSignUpMode,
                    onSocialAuthProviderClick: viewModel.signIn(with:),
                    onContinueWithEmailClick: {
                        viewModel.logClickedContinueWithEmailEvent()
                        presentingAuthWithEmail = true
                    }
                )
                NavigationLink(
                    isActive: $presentingAuthWithEmail,
                    destination: AuthCredentialsAssembly(output: viewModel.moduleOutput).makeModule,
                    label: { EmptyView() }
                )
            }
            .navigationBarHidden(true)
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

    private func renderProgressHUD(state: AuthSocialFeatureState) {
        if state is AuthSocialFeatureStateLoading {
            ProgressHUD.show()
        } else if state is AuthSocialFeatureStateError {
            ProgressHUD.showError()
        } else if state is AuthSocialFeatureStateAuthenticated {
            ProgressHUD.showSuccess()
        }
    }

    private func handleViewAction(_ viewAction: AuthSocialFeatureActionViewAction) {
        switch AuthSocialFeatureActionViewActionKs(viewAction) {
        case .completeAuthFlow(let data):
            viewModel.doCompleteAuthFlow(profile: data.profile)
        }
    }
}

struct AuthView_Previews: PreviewProvider {
    static var previews: some View {
        AuthSocialAssembly()
            .makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))

        AuthSocialAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))
            .preferredColorScheme(.dark)

        AuthSocialAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
    }
}
