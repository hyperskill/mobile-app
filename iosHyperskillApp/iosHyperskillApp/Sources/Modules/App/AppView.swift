import shared
import SwiftUI

struct AppView: View {
    @ObservedObject private var viewModel: AppViewModel

    @ObservedObject private var navigationState = AppNavigationState()

    @Environment(\.colorScheme) private var colorScheme

    @StateObject private var panModalPresenter: PanModalPresenter

    init(viewModel: AppViewModel, panModalPresenter: PanModalPresenter) {
        self.viewModel = viewModel
        self._panModalPresenter = StateObject(wrappedValue: panModalPresenter)
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        buildBody()
            .onAppear {
                viewModel.startListening()
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
        case is AppFeatureStateReady:
            TabView(selection: $navigationState.selectedTab) {
                ForEach(AppTabItem.allCases, id: \.self) { tab in
                    // TODO: Refactor to factory when Xcode 14 released
                    TabNavigationLazyView(
                        Group {
                            switch tab {
                            case .home:
                                HomeAssembly().makeModule()
                            case .track:
                                TrackAssembly(trackID: 18).makeModule()
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
            .fullScreenCover(isPresented: $navigationState.presentingAuthScreen) {
                AuthSocialAssembly(navigationState: navigationState)
                    .makeModule()
            }
            .fullScreenCover(isPresented: $navigationState.presentingNewUserScreen) {
                AuthNewUserPlaceholderView()
            }
            .environmentObject(panModalPresenter)
        default:
            ProgressView()
        }
    }

    private func updateProgressHUDStyle(colorScheme: ColorScheme) {
        ProgressHUD.updateStyle(isDark: colorScheme == .dark)
    }

    private func handleViewAction(_ viewAction: AppFeatureActionViewAction) {
        switch viewAction {
        case is AppFeatureActionViewActionNavigateToHomeScreen:
            withAnimation {
                navigationState.presentingAuthScreen = false
            }
        case is AppFeatureActionViewActionNavigateToAuthScreen:
            withAnimation {
                navigationState.presentingAuthScreen = true
            }
        case is AppFeatureActionViewActionNavigateToNewUserScreen:
            withAnimation {
            }
        default:
            print("AppView :: unhandled viewAction = \(viewAction)")
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        AppAssembly().makeModule()
    }
}
