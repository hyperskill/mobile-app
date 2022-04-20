import shared
import SwiftUI

extension AuthSocialView {
    struct Appearance {
        let logoSize: CGFloat = 48
    }
}

struct AuthSocialView: View {
    let appearance: Appearance

    @ObservedObject private var viewModel: AuthSocialViewModel

    @State private var presentingContinueWithEmail = false

    init(viewModel: AuthSocialViewModel, appearance: Appearance = Appearance()) {
        self.viewModel = viewModel
        self.appearance = appearance
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        let state = viewModel.state

        if state is AuthFeatureStateLoading {
            ProgressHUD.show()
        } else if state is AuthFeatureStateAuthenticated {
            ProgressHUD.showSuccess()
        }

        return AuthAdaptiveContentView { horizontalSizeClass in
            AuthLogoView(logoWidthHeight: appearance.logoSize)
                .padding(horizontalSizeClass == .regular ? .bottom : .vertical, appearance.logoSize)

            AuthSocialControlsView(
                socialAuthProviders: viewModel.availableSocialAuthProviders,
                onSocialAuthProviderClick: viewModel.signInWithSocialAuthProvider(_:),
                onContinueWithEmailClick: { presentingContinueWithEmail = true }
            )
            .fullScreenCover(isPresented: $presentingContinueWithEmail) {
                AuthEmailView(presentingContinueWithEmail: $presentingContinueWithEmail)
            }
        }
        .onAppear(perform: viewModel.startListening)
        .onDisappear(perform: viewModel.stopListening)
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: AuthFeatureActionViewAction) {
        if let showAuthErrorViewAction = viewAction as? AuthFeatureActionViewActionShowAuthError {
            ProgressHUD.showError(status: showAuthErrorViewAction.errorMsg)
        }
    }
}

struct AuthView_Previews: PreviewProvider {
    static var previews: some View {
        AuthSocialAssembly()
            .makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))
            .preferredColorScheme(.light)

        if #available(iOS 15.0, *) {
            AuthSocialAssembly()
                .makeModule()
                .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))
                .preferredColorScheme(.light)
                .previewInterfaceOrientation(.landscapeRight)
        }

        AuthSocialAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))
            .preferredColorScheme(.dark)

        AuthSocialAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))

        AuthSocialAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
    }
}
