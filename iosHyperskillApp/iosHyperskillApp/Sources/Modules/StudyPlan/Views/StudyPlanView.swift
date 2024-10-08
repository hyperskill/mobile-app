import shared
import SwiftUI

extension StudyPlanView {
    struct Appearance {
        let backgroundColor = Color.systemGroupedBackground

        let trackTitleBottomPadding: CGFloat = 12
    }
}

struct StudyPlanView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: StudyPlanViewModel

    let stackRouter: StackRouterProtocol
    let modalRouter: ModalRouterProtocol
    let panModalPresenter: PanModalPresenter

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(
                onViewDidAppear: {
                    viewModel.logViewedEvent()
                    viewModel.doScreenBecomesActive()
                }
            )

            BackgroundView(color: appearance.backgroundColor)

            buildBody()
                .animation(.default, value: viewModel.state)
        }
        .navigationTitle(Strings.StudyPlan.title)
        .navigationViewStyle(StackNavigationViewStyle())
        .toolbar {
            GamificationToolbarContent(
                viewStateKs: viewModel.gamificationToolbarViewStateKs,
                onStreakTap: viewModel.doStreakBarButtonItemAction,
                onProgressTap: viewModel.doProgressBarButtonItemAction,
                onProblemsLimitTap: viewModel.doProblemsLimitBarButtonItemAction,
                onSearchTap: viewModel.doSearchBarButtonItemAction
            )
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear {
            viewModel.stopListening()
            viewModel.onViewAction = nil
        }
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.studyPlanWidgetStateKs {
        case .idle:
            StudyPlanSkeletonView()
                .onAppear {
                    viewModel.doLoadStudyPlan()
                }
        case .loading:
            StudyPlanSkeletonView()
        case .error:
            PlaceholderView(
                configuration: .networkError(
                    backgroundColor: appearance.backgroundColor,
                    action: viewModel.doRetryContentLoading
                )
            )
        case .content(let data):
            ScrollView {
                LazyVStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
                    if let trackTitle = viewModel.state.trackTitle {
                        Button(action: viewModel.doTrackSelectionPresentation) {
                            HStack {
                                Text(trackTitle)

                                Image(systemName: "arrow.left.arrow.right.square")
                                    .imageScale(.large)
                            }
                        }
                        .font(.subheadline)
                        .foregroundColor(.secondaryText)
                        .padding(.bottom, appearance.trackTitleBottomPadding)
                    }

                    let usersInterviewWidgetFeatureStateKs = viewModel.usersInterviewWidgetStateKs
                    if usersInterviewWidgetFeatureStateKs != .hidden {
                        UsersInterviewWidgetAssembly(
                            stateKs: usersInterviewWidgetFeatureStateKs,
                            moduleOutput: viewModel
                        )
                        .makeModule()
                    }

                    if viewModel.notificationDailyStudyReminderWidgetViewStateKs != .hidden {
                        NotificationDailyStudyReminderWidgetView(
                            onCallToAction: viewModel.doNotificationDailyStudyReminderWidgetCallToAction,
                            onClose: viewModel.doNotificationDailyStudyReminderWidgetCloseAction,
                            onViewedEvent: viewModel.logNotificationDailyStudyReminderWidgetViewedEvent
                        )
                    }

                    if data.isPaywallBannerShown {
                        StudyPlanPaywallBanner(
                            action: viewModel.doPaywallBannerAction
                        )
                    }

                    ForEach(data.sections, id: \.id) { section in
                        StudyPlanSectionView(
                            section: section,
                            onSectionTap: viewModel.doSectionToggle(sectionId:),
                            onActivityTap: viewModel.doActivityPresentation(activityID:sectionID:),
                            onRetryActivitiesLoadingTap: viewModel.doRetryActivitiesLoading(sectionId:),
                            onLoadMoreActivitiesTap: viewModel.doLoadMoreActivities(sectionID:),
                            onExpandCompletedActivitiesTap: viewModel.doExpandCompletedActivities(sectionID:)
                        )
                    }
                }
                .padding([.horizontal, .bottom])
                .pullToRefresh(
                    isShowing: Binding(
                        get: { viewModel.state.isRefreshing },
                        set: { _ in }
                    ),
                    onRefresh: viewModel.doPullToRefresh
                )
            }
            .frame(maxWidth: .infinity)
        }
    }
}

// MARK: - StudyPlanView (ViewAction) -

private extension StudyPlanView {
    func handleViewAction(
        _ viewAction: StudyPlanScreenFeatureActionViewAction
    ) {
        switch StudyPlanScreenFeatureActionViewActionKs(viewAction) {
        case .navigateTo(let navigateToViewAction):
            handleNavigateToViewAction(navigateToViewAction)
        case .gamificationToolbarViewAction(let gamificationToolbarViewAction):
            GamificationToolbarViewActionHandler.handle(
                viewAction: gamificationToolbarViewAction.viewAction,
                stackRouter: stackRouter,
                panModalPresenter: panModalPresenter
            )
        case .studyPlanWidgetViewAction(let studyPlanWidgetViewAction):
            handleStudyPlanWidgetViewAction(
                studyPlanWidgetViewAction.viewAction
            )
        case .usersInterviewWidgetViewAction(let usersInterviewWidgetViewAction):
            handleUsersInterviewWidgetViewAction(
                usersInterviewWidgetViewAction.viewAction
            )
        case .notificationDailyStudyReminderWidgetViewAction(let notificationDailyStudyReminderWidgetViewAction):
            handleNotificationDailyStudyReminderWidgetViewAction(
                notificationDailyStudyReminderWidgetViewAction.viewAction
            )
        }
    }

    func handleNavigateToViewAction(
        _ viewAction: StudyPlanScreenFeatureActionViewActionNavigateTo
    ) {
        switch StudyPlanScreenFeatureActionViewActionNavigateToKs(viewAction) {
        case .trackSelectionScreen:
            let assembly = TrackSelectionListAssembly(isNewUserMode: false)
            stackRouter.pushViewController(assembly.makeModule())
        }
    }

    func handleStudyPlanWidgetViewAction(
        _ viewAction: StudyPlanWidgetFeatureActionViewAction
    ) {
        switch StudyPlanWidgetFeatureActionViewActionKs(viewAction) {
        case .navigateTo(let navigateToViewAction):
            handleStudyPlanWidgetNavigateToViewAction(navigateToViewAction)
        }
    }

    func handleStudyPlanWidgetNavigateToViewAction(
        _ viewAction: StudyPlanWidgetFeatureActionViewActionNavigateTo
    ) {
        switch StudyPlanWidgetFeatureActionViewActionNavigateToKs(viewAction) {
        case .home:
            TabBarRouter(tab: .home).route()
        case .learningActivityTarget(let navigateToLearningActivityTargetViewAction):
            handleNavigateToLearningActivityTargetViewAction(
                navigateToLearningActivityTargetViewAction.viewAction
            )
        case .paywall(let navigateToPaywallViewAction):
            let assembly = PaywallAssembly(
                source: navigateToPaywallViewAction.paywallTransitionSource
            )
            modalRouter.present(module: assembly.makeModule())
        }
    }

    func handleNavigateToLearningActivityTargetViewAction(
        _ viewAction: LearningActivityTargetViewAction
    ) {
        switch LearningActivityTargetViewActionKs(viewAction) {
        case .showStageImplementIDERequiredModal:
            let panModal = StageImplementUnsupportedModalViewController(delegate: viewModel)
            panModalPresenter.presentPanModal(panModal)
        case .navigateTo(let navigateToViewAction):
            switch LearningActivityTargetViewActionNavigateToKs(navigateToViewAction) {
            case .selectProject(let navigateToSelectProjectViewAction):
                let assembly = ProjectSelectionListAssembly(
                    isNewUserMode: false,
                    trackID: navigateToSelectProjectViewAction.trackId
                )
                stackRouter.pushViewController(assembly.makeModule())
            case .selectTrack:
                let assembly = TrackSelectionListAssembly(isNewUserMode: false)
                stackRouter.pushViewController(assembly.makeModule())
            case .stageImplement(let navigateToStageImplementViewAction):
                let assembly = StageImplementAssembly(
                    projectID: navigateToStageImplementViewAction.projectId,
                    stageID: navigateToStageImplementViewAction.stageId
                )
                stackRouter.pushViewController(assembly.makeModule())
            case .step(let navigateToStepViewAction):
                let assembly = StepAssembly(
                    stepRoute: navigateToStepViewAction.stepRoute
                )
                stackRouter.pushViewController(assembly.makeModule())
            }
        }
    }

    func handleUsersInterviewWidgetViewAction(
        _ viewAction: UsersInterviewWidgetFeatureActionViewAction
    ) {
        switch UsersInterviewWidgetFeatureActionViewActionKs(viewAction) {
        case .showUsersInterview(let showUsersInterviewViewAction):
            WebControllerManager.shared.presentWebControllerWithURLString(
                showUsersInterviewViewAction.url,
                controllerType: .inAppSafari
            )
        }
    }

    func handleNotificationDailyStudyReminderWidgetViewAction(
        _ viewAction: NotificationDailyStudyReminderWidgetFeatureActionViewAction
    ) {
        switch NotificationDailyStudyReminderWidgetFeatureActionViewActionKs(viewAction) {
        case .requestNotificationPermission:
            viewModel.doNotificationDailyStudyReminderWidgetRequestNotificationPermission()
        }
    }
}
