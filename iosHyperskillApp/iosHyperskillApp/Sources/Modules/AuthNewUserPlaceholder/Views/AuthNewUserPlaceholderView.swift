import shared
import SwiftUI

extension AuthNewUserPlaceholderView {
    struct Appearance {
        let logoWidthHeight: CGFloat = 48
        let verticalContentPadding: CGFloat = 40
        let largePadding: CGFloat = 32
        let backgroundColor = Color.background
    }
}

struct AuthNewUserPlaceholderView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: AuthNewUserPlaceholderViewModel

    let dataMapper: PlaceholderNewUserViewDataMapper

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: appearance.backgroundColor)

            buildBody()
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear(perform: viewModel.stopListening)
    }

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.stateKs {
        case .idle:
            ProgressView()
                .onAppear {
                    viewModel.doLoadContent()
                }
        case .loading:
            ProgressView()
        case .networkError:
            PlaceholderView(
                configuration: .networkError(backgroundColor: appearance.backgroundColor) {
                    viewModel.doLoadContent(forceUpdate: true)
                }
            )
        case .content(let contentState):
            buildContent(contentState: contentState)
        }
    }

    @ViewBuilder
    private func buildContent(contentState: PlaceholderNewUserFeatureStateContent) -> some View {
        let viewData = dataMapper.mapStateToViewData(state: contentState)

        ScrollView {
            VStack(alignment: .leading, spacing: appearance.largePadding) {
                VStack(spacing: LayoutInsets.largeInset) {
                    HyperskillLogoView(logoWidthHeight: appearance.logoWidthHeight)

                    Text(Strings.Auth.NewUserPlaceholder.title)
                        .font(.title2)
                        .foregroundColor(.primaryText)
                        .bold()
                        .multilineTextAlignment(.center)
                }
                .frame(maxWidth: .infinity)

                Text(Strings.Auth.NewUserPlaceholder.text)
                    .font(.body)
                    .foregroundColor(.primaryText)

                VStack(spacing: LayoutInsets.smallInset) {
                    ForEach(viewData.tracks, id: \.id) { track in
                        AuthNewUserPlaceholderTrackCardView(
                            imageSource: track.imageSource,
                            title: track.title,
                            timeToComplete: track.timeToComplete,
                            rating: track.rating
                        )
                    }
                }
            }
            .padding(.horizontal)
            .padding(.vertical, appearance.verticalContentPadding)
        }
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: PlaceholderNewUserFeatureActionViewAction) {
        //        switch PlaceholderNewUserFeatureActionViewActionKs(viewAction) {
        //        case .navigateTo(let navigateToViewAction):
        //            switch PlaceholderNewUserFeatureActionViewActionNavigateToKs(navigateToViewAction) {
        //            case .authScreen:
        //                viewModel.doAuthScreenPresentation()
        //            }
        //        case .openUrl(let data):
        //            ProgressHUD.showSuccess()
        //            WebControllerManager.shared.presentWebControllerWithURLString(data.url, controllerType: .inAppSafari)
        //        case .showGetMagicLinkError:
        //            ProgressHUD.showError()
        //        }
    }
}

struct AuthNewUserPlaceholderView_Previews: PreviewProvider {
    static var previews: some View {
        AuthNewUserPlaceholderAssembly().makeModule()

        AuthNewUserPlaceholderAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
    }
}
