import shared
import SwiftUI

struct AppView: View {
    @ObservedObject var viewModel: AppViewModel

    private(set) var panModalPresenter: PanModalPresenter

    @Environment(\.colorScheme) private var colorScheme

    var body: some View {
        buildBody()
            .onAppear {
                viewModel.startListening()
                viewModel.onViewAction = handleViewAction(_:)

                updateProgressHUDStyle(colorScheme: colorScheme)
            }
            .onDisappear {
                viewModel.stopListening()
            }
            .onChange(of: colorScheme) { newColorScheme in
                updateProgressHUDStyle(colorScheme: newColorScheme)
            }
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        let state = viewModel.state

        switch state {
        case is AppFeatureStateIdle:
            ProgressView()
                .onAppear {
                    viewModel.loadApp()
                }
        case is AppFeatureStateLoading:
            ProgressView()
        case is AppFeatureStateNetworkError:
            PlaceholderView(
                configuration: .networkError {
                    viewModel.loadApp(forceUpdate: true)
                }
            )
        case let readyState as AppFeatureStateReady:
            buildContent(
                isAuthorized: readyState.isAuthorized,
                trackID: {
                    guard let trackID = readyState.trackID else {
                        return nil
                    }
                    return Int(truncating: trackID)
                }()
            )
            .fullScreenCover(item: $viewModel.navigationState.activeFullScreenModal) { screen in
                switch screen {
                case .auth:
                    AuthSocialAssembly(output: viewModel).makeModule()
                case .onboarding:
                    OnboardingAssembly(output: viewModel).makeModule()
                case .newUser:
                    AuthNewUserPlaceholderView()
                }
            }
            .environmentObject(panModalPresenter)
        default:
            ProgressView()
        }
    }

    @ViewBuilder
    private func buildContent(isAuthorized: Bool, trackID: Int?) -> some View {
        if isAuthorized {
            TabView(selection: $viewModel.navigationState.selectedTab) {
                ForEach(AppTabItem.allCases, id: \.self) { tab in
                    // TODO: Refactor to factory when Xcode 14 released
                    TabNavigationLazyView(
                        Group {
                            switch tab {
                            case .home:
                                HomeAssembly().makeModule()
                            case .track:
                                if let trackID = trackID {
                                    TrackAssembly(trackID: trackID).makeModule()
                                }
                            case .profile:
                                ProfileAssembly.currentUser().makeModule()
                            }
                        }
                    )
                    .tag(tab)
                    .tabItem {
                        Image(tab.imageName)
                            .renderingMode(.template)
                        Text(tab.title)
                    }
                }
            }
        } else {
            ProgressView()
        }
    }

    private func updateProgressHUDStyle(colorScheme: ColorScheme) {
        ProgressHUD.updateStyle(isDark: colorScheme == .dark)
    }

    private func handleViewAction(_ viewAction: AppFeatureActionViewAction) {
        switch viewAction {
        case is AppFeatureActionViewActionNavigateToOnboardingScreen:
            withAnimation {
                viewModel.navigationState.activeFullScreenModal = .onboarding
            }
        case is AppFeatureActionViewActionNavigateToHomeScreen:
            withAnimation {
                viewModel.navigationState.activeFullScreenModal = nil
            }
        case is AppFeatureActionViewActionNavigateToAuthScreen:
            withAnimation {
                viewModel.navigationState.activeFullScreenModal = .auth
            }
        case is AppFeatureActionViewActionNavigateToNewUserScreen:
            withAnimation {
                viewModel.navigationState.activeFullScreenModal = .newUser
            }
        default:
            print("AppView :: unhandled viewAction = \(viewAction)")
        }
    }
}

struct AppView_Previews: PreviewProvider {
    static var previews: some View {
        AppAssembly().makeModule()
    }
}
