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

    @StateObject var stackRouter: SwiftUIStackRouter
    @StateObject var panModalPresenter: PanModalPresenter

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
        }
        .navigationTitle(Strings.StudyPlan.title)
        .navigationViewStyle(StackNavigationViewStyle())
        .toolbar {
            GamificationToolbarContent(
                stateKs: viewModel.gamificationToolbarStateKs,
                onGemsTap: viewModel.doGemsBarButtonItemAction,
                onStreakTap: viewModel.doStreakBarButtonItemAction,
                onProgressTap: viewModel.doProgressBarButtonItemAction
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
                        Text(trackTitle)
                            .font(.subheadline)
                            .foregroundColor(.secondaryText)
                            .padding(.bottom, appearance.trackTitleBottomPadding)
                    }

                    ProblemsLimitView(
                        stateKs: viewModel.problemsLimitViewStateKs,
                        onReloadButtonTap: viewModel.doReloadProblemsLimit
                    )

                    ForEach(data.sections, id: \.id) { section in
                        StudyPlanSectionView(
                            section: section,
                            onSectionTap: viewModel.doSectionToggle(sectionId:),
                            onActivityTap: viewModel.doActivityPresentation(activityId:),
                            onRetryActivitiesLoadingTap: viewModel.doRetryActivitiesLoading(sectionId:)
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
        case .gamificationToolbarViewAction(let gamificationToolbarViewAction):
            handleGamificationToolbarViewAction(
                gamificationToolbarViewAction.viewAction
            )
        case .problemsLimitViewAction:
            break
        case .studyPlanWidgetViewAction(let studyPlanWidgetViewAction):
            handleStudyPlanWidgetViewAction(
                studyPlanWidgetViewAction.viewAction
            )
        }
    }

    func handleGamificationToolbarViewAction(
        _ viewAction: GamificationToolbarFeatureActionViewAction
    ) {
        switch GamificationToolbarFeatureActionViewActionKs(viewAction) {
        case .showProfileTab:
            TabBarRouter(tab: .profile).route()
        }
    }

    func handleStudyPlanWidgetViewAction(
        _ viewAction: StudyPlanWidgetFeatureActionViewAction
    ) {
        switch StudyPlanWidgetFeatureActionViewActionKs(viewAction) {
        case .showStageImplementUnsupportedModal:
            let panModal = StageImplementUnsupportedModalViewController(delegate: viewModel)
            panModalPresenter.presentPanModal(panModal)
        case .navigateTo(let navigateToViewAction):
            handleStudyPlanWidgetNavigateToViewAction(navigateToViewAction)
        }
    }

    func handleStudyPlanWidgetNavigateToViewAction(
        _ viewAction: StudyPlanWidgetFeatureActionViewActionNavigateTo
    ) {
        switch StudyPlanWidgetFeatureActionViewActionNavigateToKs(viewAction) {
        case .stageImplement(let navigateToStageImplementViewAction):
            let assembly = StageImplementAssembly(
                projectID: navigateToStageImplementViewAction.projectId,
                stageID: navigateToStageImplementViewAction.stageId
            )
            stackRouter.pushViewController(assembly.makeModule())
        case .stepScreen(let navigateToStepScreenViewAction):
            let assembly = StepAssembly(
                stepRoute: navigateToStepScreenViewAction.stepRoute
            )
            stackRouter.pushViewController(assembly.makeModule())
        case .home:
            TabBarRouter(tab: .home).route()
        case .selectProject(let navigateToSelectProjectViewAction):
            let assembly = ProjectSelectionListAssembly(
                trackID: navigateToSelectProjectViewAction.trackId
            )
            stackRouter.pushViewController(assembly.makeModule())
        case .selectTrack:
            let assembly = TrackSelectionListAssembly()
            stackRouter.pushViewController(assembly.makeModule())
        }
    }
}

// MARK: - StudyPlanView_Previews: PreviewProvider -

struct StudyPlanView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            StudyPlanAssembly().makeModule()
        }
    }
}
