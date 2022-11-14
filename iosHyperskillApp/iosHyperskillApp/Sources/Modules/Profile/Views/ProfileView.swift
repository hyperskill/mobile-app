import shared
import SwiftUI

extension ProfileView {
    struct Appearance {
        let spacingBetweenContainers: CGFloat = 20

        let backgroundColor = Color.systemGroupedBackground
    }
}

struct ProfileView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: ProfileViewModel

    @State private var presentingSettings = false

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: appearance.backgroundColor)

            buildBody()
        }
        .navigationTitle(Strings.Profile.title)
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                Button(
                    action: {
                        viewModel.logClickedSettingsEvent()
                        presentingSettings = true
                    },
                    label: { Image(systemName: "gear") }
                )
            }
        }
        .sheet(isPresented: $presentingSettings) {
            ProfileSettingsAssembly().makeModule()
        }
        .onAppear(perform: viewModel.determineCurrentNotificationPermissionStatus)
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear(perform: viewModel.stopListening)
        .navigationViewStyle(StackNavigationViewStyle())
        .environmentObject(PanModalPresenter())
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.state {
        case is ProfileFeatureStateIdle:
            ProgressView()
                .onAppear {
                    viewModel.doLoadProfile()
                }
        case is ProfileFeatureStateLoading:
            ProgressView()
        case is ProfileFeatureStateError:
            PlaceholderView(
                configuration: .networkError(backgroundColor: appearance.backgroundColor) {
                    viewModel.doLoadProfile(forceUpdate: true)
                }
            )
        case let state as ProfileFeatureStateContent:
            let viewData = viewModel.makeViewData(state.profile)

            ScrollView {
                VStack(spacing: appearance.spacingBetweenContainers) {
                    ProfileHeaderView(
                        avatarSource: viewData.avatarSource,
                        title: viewData.fullname,
                        subtitle: viewData.role
                    )

                    if let streak = state.streak {
                        StreakViewBuilder(streak: streak, viewType: .plain)
                            .build()
                            .padding()
                            .background(Color(ColorPalette.surface))
                    }

                    ProfileDailyStudyRemindersView(
                        isActivated: viewData.isDailyStudyRemindersEnabled,
                        selectedHour: viewData.dailyStudyRemindersStartHour,
                        onIsActivatedChanged: viewModel.setDailyStudyRemindersEnabled(_:),
                        onSelectedHourChanged: viewModel.setDailyStudyRemindersStartHour(startHour:),
                        onSelectedHourTapped: viewModel.logClickedDailyStudyRemindsTimeEvent
                    )

                    ProfileAboutView(
                        livesInText: viewData.livesInText,
                        speaksText: viewData.speaksText,
                        bio: viewData.bio,
                        experience: viewData.experience,
                        socialAccounts: viewData.socialAccounts,
                        onSocialAccountTapped: viewModel.presentSocialAccount(_:),
                        onFullVersionButtonTapped: viewModel.presentProfileFullVersion
                    )
                }
                .padding(.vertical)
                .pullToRefresh(
                    isShowing: Binding(
                        get: { state.isRefreshing },
                        set: { _ in }
                    ),
                    onRefresh: viewModel.doPullToRefresh
                )
            }
            .frame(maxWidth: .infinity)
        default:
            Text("Unkwown state")
        }
    }

    private func handleViewAction(_ viewAction: ProfileFeatureActionViewAction) {
        print("ProfileView :: \(#function) viewAction = \(viewAction)")
    }
}

struct ProfileView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileAssembly.currentUser().makeModule()
    }
}
