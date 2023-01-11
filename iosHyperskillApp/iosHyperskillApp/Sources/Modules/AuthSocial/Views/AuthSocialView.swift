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
        let state = viewModel.state

        if state is AuthSocialFeatureStateLoading {
            ProgressHUD.show()
        } else if state is AuthSocialFeatureStateAuthenticated {
            ProgressHUD.showSuccess()
        }

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
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear(perform: viewModel.stopListening)
        .navigationViewStyle(StackNavigationViewStyle())
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: AuthSocialFeatureActionViewAction) {
        switch AuthSocialFeatureActionViewActionKs(viewAction) {
        case .completeAuthFlow(let data):
            viewModel.doCompleteAuthFlow(profile: data.profile)
        case .showAuthError(let data):
            let errorText = viewModel.getAuthSocialErrorText(authSocialError: data.socialAuthError)
            ProgressHUD.showError(status: errorText)
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
