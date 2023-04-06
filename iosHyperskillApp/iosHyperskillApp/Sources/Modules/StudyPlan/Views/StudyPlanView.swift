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

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: appearance.backgroundColor)

            buildBody()
        }
        .navigationTitle(Strings.StudyPlan.title)
        .navigationViewStyle(StackNavigationViewStyle())
        .toolbar {
            GamificationToolbarContent(
                stateKs: viewModel.gamificationToolbarStateKs,
                onGemsTap: viewModel.doGemsBarButtonItemAction,
                onStreakTap: viewModel.doStreakBarButtonItemAction
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

                    ProblemsLimitAssembly().makeModule()

                    ForEach(data.sections, id: \.id) { section in
                        StudyPlanSectionView(
                            section: section,
                            onSectionTap: viewModel.doSectionToggle(sectionId:),
                            onActivityTap: viewModel.doActivityPresentation(activityId:)
                        )
                    }
                }
                .padding([.horizontal, .bottom])
            }
            .frame(maxWidth: .infinity)
        }
    }

    private func handleViewAction(_ viewAction: StudyPlanScreenFeatureActionViewAction) {
        switch StudyPlanScreenFeatureActionViewActionKs(viewAction) {
        case .gamificationToolbarViewAction(let gamificationToolbarViewAction):
            switch GamificationToolbarFeatureActionViewActionKs(gamificationToolbarViewAction.viewAction) {
            case .showProfileTab:
                TabBarRouter(tab: .profile).route()
            }
        case .studyPlanWidgetViewAction(let studyPlanWidgetViewAction):
            switch StudyPlanWidgetFeatureActionViewActionKs(studyPlanWidgetViewAction.viewAction) {
            case .navigateTo(let navigateToViewAction):
                switch StudyPlanWidgetFeatureActionViewActionNavigateToKs(navigateToViewAction) {
                case .stageImplementation(let navigateToStageImplementationViewAction):
                    let assembly = StageImplementAssembly(
                        projectID: navigateToStageImplementationViewAction.projectId,
                        stageID: navigateToStageImplementationViewAction.stageId
                    )
                    stackRouter.pushViewController(assembly.makeModule())
                }
            }
        }
    }
}

struct StudyPlanView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            StudyPlanAssembly().makeModule()
        }
    }
}
