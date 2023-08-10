import shared
import SwiftUI

extension ProfileView {
    struct Appearance {
        let spacingBetweenContainers: CGFloat = 20

        let cornerRadius: CGFloat = 8

        let backgroundColor = Color.systemGroupedBackground
    }
}

struct ProfileView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: ProfileViewModel

    private(set) var panModalPresenter: PanModalPresenter

    @State private var presentingSettings = false

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(
                onViewDidAppear: { [weak viewModel] in
                    guard let strongViewModel = viewModel else {
                        return
                    }

                    strongViewModel.logViewedEvent()
                    strongViewModel.determineCurrentNotificationPermissionStatus()
                }
            )

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
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear {
            viewModel.stopListening()
            viewModel.onViewAction = nil
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .environmentObject(PanModalPresenter())
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.stateKs {
        case .idle:
            ProfileSkeletonView()
                .onAppear {
                    viewModel.doLoadProfile()
                }
        case .loading:
            ProfileSkeletonView()
        case .error:
            PlaceholderView(
                configuration: .networkError(backgroundColor: appearance.backgroundColor) {
                    viewModel.doLoadProfile(forceUpdate: true)
                }
            )
        case .content(let data):
            if data.isLoadingMagicLink {
                let _ = ProgressHUD.show()
            }

            let profileViewData = viewModel.makeProfileViewData(
                profile: data.profile,
                dailyStudyRemindersState: data.dailyStudyRemindersState
            )

            ScrollView {
                VStack(spacing: appearance.spacingBetweenContainers) {
                    ProfileHeaderView(
                        appearance: .init(cornerRadius: appearance.cornerRadius),
                        avatarSource: profileViewData.avatarSource,
                        title: profileViewData.fullname,
                        subtitle: profileViewData.role
                    )

                    if let streak = data.streak {
                        StreakViewBuilder(
                            streak: streak,
                            streakFreezeState: data.streakFreezeState,
                            onStreakFreezeTapped: viewModel.doStreakFreezeCardButtonTapped,
                            viewType: .plain
                        )
                        .build()
                        .padding()
                        .background(Color(ColorPalette.surface))
                        .cornerRadius(appearance.cornerRadius)
                    }

                    ProfileDailyStudyRemindersView(
                        appearance: .init(cornerRadius: appearance.cornerRadius),
                        isActivated: profileViewData.isDailyStudyRemindersEnabled,
                        selectedHour: profileViewData.dailyStudyRemindersStartHour,
                        onIsActivatedChanged: viewModel.setDailyStudyRemindersEnabled(_:),
                        onSelectedHourChanged: viewModel.setDailyStudyRemindersStartHour(_:),
                        onSelectedHourTapped: viewModel.logClickedDailyStudyRemindsTimeEvent
                    )

                    ProfileBadgesGridView(
                        appearance: .init(cornerRadius: appearance.cornerRadius),
                        badgesState: viewModel.makeBadgesViewState(badgesState: data.badgesState),
                        onBadgeTap: viewModel.doBadgeCardTapped(badgeKind:),
                        onVisibilityButtonTap: viewModel.doBadgesVisibilityButtonTapped(visibilityButton:)
                    )

                    ProfileStatisticsView(
                        appearance: .init(cornerRadius: appearance.cornerRadius),
                        passedProjectsCount: Int(data.profile.gamification.passedProjectsCount),
                        passedTracksCount: Int(data.profile.completedTracks.count),
                        hypercoinsBalance: Int(data.profile.gamification.hypercoinsBalance)
                    )

                    ProfileAboutView(
                        appearance: .init(cornerRadius: appearance.cornerRadius),
                        livesInText: profileViewData.livesInText,
                        speaksText: profileViewData.speaksText,
                        bio: profileViewData.bio,
                        experience: profileViewData.experience,
                        socialAccounts: profileViewData.socialAccounts,
                        onSocialAccountTapped: viewModel.doSocialAccountPresentation(_:),
                        onFullVersionButtonTapped: viewModel.doProfileFullVersionPresentation
                    )
                }
                .padding()
                .pullToRefresh(
                    isShowing: Binding(
                        get: { data.isRefreshing },
                        set: { _ in }
                    ),
                    onRefresh: viewModel.doPullToRefresh
                )
            }
            .frame(maxWidth: .infinity)
        }
    }

    private func handleViewAction(_ viewAction: ProfileFeatureActionViewAction) {
        switch ProfileFeatureActionViewActionKs(viewAction) {
        case .openUrl(let data):
            ProgressHUD.showSuccess()
            WebControllerManager.shared.presentWebControllerWithURLString(data.url)
        case .showGetMagicLinkError:
            ProgressHUD.showError()
        case .showStreakFreezeBuyingStatus(let streakFreezeBuyingStatus):
            switch ProfileFeatureActionViewActionShowStreakFreezeBuyingStatusKs(streakFreezeBuyingStatus) {
            case .loading:
                ProgressHUD.show()
            case .success:
                ProgressHUD.showSuccess(status: Strings.Streak.FreezeModal.boughtSuccess)
            case .error:
                ProgressHUD.showError(status: Strings.Streak.FreezeModal.boughtError)
            }
        case .showStreakFreezeModal(let actionShowStreakFreezeModal):
            displayStreakFreezeModal(
                streakFreezeState: ProfileFeatureStreakFreezeStateKs(actionShowStreakFreezeModal.streakFreezeState)
            )
        case .hideStreakFreezeModal:
            panModalPresenter.dismissPanModal()
        case .navigateTo(let actionNavigateTo):
            switch ProfileFeatureActionViewActionNavigateToKs(actionNavigateTo) {
            case .homeScreen:
                TabBarRouter(tab: .home).route()
            }
        case .showBadgeDetailsModal(let showBadgeDetailsModalViewAction):
            displayBadgeDetailsModal(details: showBadgeDetailsModalViewAction.details)
        }
    }

    private func displayBadgeDetailsModal(details: ProfileFeatureActionViewActionBadgeDetails) {
        let assembly = BadgeDetailsModalAssembly(badgeDetails: details, delegate: viewModel)
        panModalPresenter.presentIfPanModal(assembly.makeModule())
    }

    private func displayStreakFreezeModal(streakFreezeState: ProfileFeatureStreakFreezeStateKs) {
        viewModel.logStreakFreezeModalShownEvent()

        let panModal = StreakFreezeModalViewController(
            streakFreezeState: streakFreezeState,
            onActionButtonTap: viewModel.doStreakFreezeModalButtonTapped
        )
        panModal.onDisappear = { [weak viewModel] in
            viewModel?.logStreakFreezeModalHiddenEvent()
        }

        panModalPresenter.presentPanModal(panModal)
    }
}

struct ProfileView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileAssembly.currentUser().makeModule()
    }
}
