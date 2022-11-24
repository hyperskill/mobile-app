import shared
import SwiftUI

extension AuthNewUserPlaceholderView {
    struct Appearance {
        let logoWidthHeight: CGFloat = 48

        let spacingLarge: CGFloat = 48
        let spacingSmall: CGFloat = 20

        let contentMaxWidth: CGFloat = 400
    }
}

struct AuthNewUserPlaceholderView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: AuthNewUserPlaceholderViewModel

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    var body: some View {
        if case .content(let data) = viewModel.stateKs, data.isLoadingMagicLink {
            ProgressHUD.show()
        }

        return ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView()

            VStack(spacing: 0) {
                if horizontalSizeClass == .regular {
                    Spacer()
                }

                content

                Spacer()
            }
            .frame(maxWidth: appearance.contentMaxWidth)
            .padding()
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear(perform: viewModel.stopListening)
    }

    private var content: some View {
        VStack(alignment: .leading, spacing: appearance.spacingLarge) {
            VStack(alignment: .center, spacing: appearance.spacingSmall) {
                HyperskillLogoView(logoWidthHeight: appearance.logoWidthHeight)

                Text(Strings.Auth.NewUserPlaceholder.title)
                    .font(.title2)
                    .foregroundColor(.primaryText)
                    .multilineTextAlignment(.center)
            }
            .frame(maxWidth: .infinity)

            Text(Strings.Auth.NewUserPlaceholder.introText)
                .font(.body)
                .foregroundColor(.primaryText)

            VStack(spacing: appearance.spacingSmall) {
                Button(
                    Strings.Auth.NewUserPlaceholder.continueButton,
                    action: viewModel.doContinueOnWebPresentation
                )
                .buttonStyle(RoundedRectangleButtonStyle(style: .violet))

                Button(
                    Strings.Auth.NewUserPlaceholder.signInButton,
                    action: viewModel.doSignIn
                )
                .buttonStyle(OutlineButtonStyle())
            }

            Text(Strings.Auth.NewUserPlaceholder.possibilityText)
                .font(.body)
                .foregroundColor(.primaryText)
        }
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: PlaceholderNewUserFeatureActionViewAction) {
        switch PlaceholderNewUserFeatureActionViewActionKs(viewAction) {
        case .navigateTo(let navigateToViewAction):
            switch PlaceholderNewUserFeatureActionViewActionNavigateToKs(navigateToViewAction) {
            case .authScreen:
                viewModel.doAuthScreenPresentation()
            }
        case .openUrl(let data):
            ProgressHUD.showSuccess()
            WebControllerManager.shared.presentWebControllerWithURLString(data.url, controllerType: .inAppSafari)
        case .showGetMagicLinkError:
            ProgressHUD.showError()
        }
    }
}

struct AuthNewUserPlaceholderView_Previews: PreviewProvider {
    static var previews: some View {
        AuthNewUserPlaceholderAssembly().makeModule()

        AuthNewUserPlaceholderAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
    }
}
