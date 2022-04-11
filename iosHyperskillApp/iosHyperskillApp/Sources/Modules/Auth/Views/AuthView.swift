import shared
import SwiftUI

extension AuthView {
    struct Appearance {
        let logoSize: CGFloat = 48

        let contentMaxWidth: CGFloat = 400
    }
}

struct AuthView: View {
    let appearance: Appearance

    @ObservedObject private var viewModel: AuthViewModel

    @State private var presentingContinueWithEmail = false

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    init(viewModel: AuthViewModel, appearance: Appearance = Appearance()) {
        self.viewModel = viewModel
        self.appearance = appearance
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        NavigationView {
            ZStack {
                Color(ColorPalette.background).ignoresSafeArea()

                VStack(spacing: 0) {
                    if horizontalSizeClass == .regular {
                        Spacer()
                    }

                    AuthLogoView(logoWidthHeight: appearance.logoSize)
                        .padding(horizontalSizeClass == .regular ? .bottom : .vertical, appearance.logoSize)

                    AuthSocialControlsView(
                        socialAuthProviders: viewModel.availableSocialAuthProviders,
                        onSocialAuthProviderClick: viewModel.signInWithSocialAuthProvider(_:),
                        onContinueWithEmailClick: { presentingContinueWithEmail = true }
                    )
                    .fullScreenCover(isPresented: $presentingContinueWithEmail) {
                        Text("Continue with email")
                    }

                    Spacer()
                }
                .frame(maxWidth: appearance.contentMaxWidth)
            }
            .navigationBarHidden(true)
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: AuthFeatureActionViewAction) {
        print("AuthView :: \(#function) viewAction = \(viewAction)")
    }
}

struct AuthView_Previews: PreviewProvider {
    static var previews: some View {
        AuthAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))
            .preferredColorScheme(.light)

        AuthAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))
            .preferredColorScheme(.dark)

        AuthAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))

        AuthAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
    }
}
