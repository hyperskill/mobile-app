import shared
import SwiftUI

extension AuthNewUserPlaceholderView {
    struct Appearance {
        let logoWidthHeight: CGFloat = 48
        let largePadding: CGFloat = 32
        let backgroundColor = Color.background
    }
}

struct AuthNewUserPlaceholderView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: AuthNewUserPlaceholderViewModel

    @StateObject var panModalPresenter = PanModalPresenter()

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
            AuthNewUserPlaceholderSkeletonView()
                .onAppear {
                    viewModel.doLoadContent()
                }
        case .loading:
            AuthNewUserPlaceholderSkeletonView()
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
            VStack(spacing: appearance.largePadding) {
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
                    .multilineTextAlignment(.center)

                VStack(spacing: LayoutInsets.smallInset) {
                    ForEach(viewData.tracks, id: \.id) { track in
                        Button(
                            action: {
                                viewModel.doTrackCardClicked(trackID: track.id)
                            },
                            label: {
                                AuthNewUserPlaceholderTrackCardView(
                                    imageSource: track.imageSource,
                                    title: track.title,
                                    timeToComplete: track.timeToComplete,
                                    rating: track.rating
                                )
                            }
                        )
                        .buttonStyle(BounceButtonStyle())
                    }
                }
            }
            .padding()
        }
    }

    // MARK: Private API

    private func handleViewAction(_ viewAction: PlaceholderNewUserFeatureActionViewAction) {
        switch PlaceholderNewUserFeatureActionViewActionKs(viewAction) {
        case .showTrackModal(let showTrackModalAction):
            let viewDataTrack = dataMapper.mapTrackToViewDataTrack(track: showTrackModalAction.track)
            presentTrackModal(track: viewDataTrack)
        case .showTrackSelectionStatus(let trackSelectionStatus):
            switch PlaceholderNewUserFeatureActionViewActionShowTrackSelectionStatusKs(trackSelectionStatus) {
            case .loading:
                ProgressHUD.show()
            case .error:
                ProgressHUD.showError(status: Strings.Auth.NewUserPlaceholder.trackSelectionErrorMessage)
            case .success:
                panModalPresenter.dismissPanModal(animated: true)
                ProgressHUD.showSuccess(status: Strings.Auth.NewUserPlaceholder.trackSelectionSuccessMessage)
            }
        case .navigateTo(let navigateToViewAction):
            switch PlaceholderNewUserFeatureActionViewActionNavigateToKs(navigateToViewAction) {
            case .homeScreen:
                viewModel.doHomeScreenPresentation()
            }
        }
    }

    private func presentTrackModal(track: PlaceholderNewUserViewData.Track) {
        viewModel.logTrackModalShownEvent(trackID: track.id)

        let panModal = AuthNewUserTrackModalViewController(
            track: track,
            onStartLearningButtonTap: {
                viewModel.doStartLearningClicked(trackID: track.id)
            }
        )

        panModal.onDisappear = { viewModel.logTrackModalHiddenEvent(trackID: track.id) }

        panModalPresenter.presentPanModal(panModal)
    }
}

struct AuthNewUserPlaceholderView_Previews: PreviewProvider {
    static var previews: some View {
        AuthNewUserPlaceholderAssembly().makeModule()

        AuthNewUserPlaceholderAssembly().makeModule()
            .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
    }
}
