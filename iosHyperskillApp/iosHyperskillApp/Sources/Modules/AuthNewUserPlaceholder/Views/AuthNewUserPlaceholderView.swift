// swiftlint:disable:next file_header
//import shared
//import SwiftUI
//
//extension AuthNewUserPlaceholderView {
//    struct Appearance {
//        let spacing: CGFloat = 32
//
//        let backgroundColor = Color.background
//    }
//}
//
//struct AuthNewUserPlaceholderView: View {
//    private(set) var appearance = Appearance()
//
//    @StateObject var viewModel: AuthNewUserPlaceholderViewModel
//
//    @StateObject var panModalPresenter = PanModalPresenter()
//
//    let viewDataMapper: PlaceholderNewUserViewDataMapper
//
//    var body: some View {
//        ZStack {
//            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)
//
//            BackgroundView(color: appearance.backgroundColor)
//
//            buildBody()
//        }
//        .onAppear {
//            viewModel.startListening()
//            viewModel.onViewAction = handleViewAction(_:)
//        }
//        .onDisappear {
//            viewModel.stopListening()
//            viewModel.onViewAction = nil
//        }
//    }
//
//    @ViewBuilder
//    private func buildBody() -> some View {
//        switch viewModel.stateKs {
//        case .idle:
//            AuthNewUserPlaceholderSkeletonView()
//                .onAppear {
//                    viewModel.doLoadContent()
//                }
//        case .loading:
//            AuthNewUserPlaceholderSkeletonView()
//        case .networkError:
//            PlaceholderView(
//                configuration: .networkError(backgroundColor: appearance.backgroundColor) {
//                    viewModel.doLoadContent(forceUpdate: true)
//                }
//            )
//        case .content(let contentState):
//            buildContent(contentState: contentState)
//        }
//    }
//
//    @ViewBuilder
//    private func buildContent(contentState: PlaceholderNewUserFeatureStateContent) -> some View {
//        let viewData = viewDataMapper.mapStateToViewData(state: contentState)
//
//        ScrollView {
//            VStack(spacing: appearance.spacing) {
//                AuthNewUserPlaceholderHeaderView(appearance: .init(spacing: appearance.spacing))
//
//                VStack(spacing: LayoutInsets.smallInset) {
//                    ForEach(viewData.tracks, id: \.id) { track in
//                        Button(
//                            action: {
//                                viewModel.doTrackModalPresentation(trackID: track.id)
//                            },
//                            label: {
//                                AuthNewUserPlaceholderTrackCardView(
//                                    imageSource: track.imageSource,
//                                    title: track.title,
//                                    timeToComplete: track.timeToComplete,
//                                    rating: track.rating
//                                )
//                            }
//                        )
//                        .buttonStyle(BounceButtonStyle())
//                    }
//                }
//            }
//            .padding()
//        }
//    }
//
//    // MARK: Private API
//
//    private func handleViewAction(_ viewAction: PlaceholderNewUserFeatureActionViewAction) {
//        switch PlaceholderNewUserFeatureActionViewActionKs(viewAction) {
//        case .showTrackModal(let data):
//            let trackViewData = viewDataMapper.mapTrackToViewDataTrack(track: data.track)
//            displayTrackModal(track: trackViewData)
//        case .showTrackSelectionStatus(let status):
//            switch PlaceholderNewUserFeatureActionViewActionShowTrackSelectionStatusKs(status) {
//            case .loading:
//                ProgressHUD.show()
//            case .error:
//                ProgressHUD.showError(status: Strings.Auth.NewUserPlaceholder.TrackSelectionStatus.error)
//            case .success:
//                ProgressHUD.showSuccess(status: Strings.Auth.NewUserPlaceholder.TrackSelectionStatus.success)
//            }
//        case .navigateTo(let navigateToViewAction):
//            switch PlaceholderNewUserFeatureActionViewActionNavigateToKs(navigateToViewAction) {
//            case .homeScreen:
//                panModalPresenter.dismissPanModal()
//                viewModel.doHomeScreenPresentation()
//            }
//        }
//    }
//
//    private func displayTrackModal(track: PlaceholderNewUserViewData.Track) {
//        viewModel.logTrackModalShownEvent(trackID: track.id)
//
//        let panModal = AuthNewUserTrackModalViewController(
//            track: track,
//            onStartLearningButtonTap: { [weak viewModel] in
//                viewModel?.doTrackStartLearningAction(trackID: track.id)
//            }
//        )
//        panModal.onDisappear = { [weak viewModel] in
//            viewModel?.logTrackModalHiddenEvent(trackID: track.id)
//        }
//
//        panModalPresenter.presentPanModal(panModal)
//    }
//}
//
//struct AuthNewUserPlaceholderView_Previews: PreviewProvider {
//    static var previews: some View {
//        AuthNewUserPlaceholderAssembly().makeModule()
//
//        AuthNewUserPlaceholderAssembly().makeModule()
//            .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
//    }
//}
