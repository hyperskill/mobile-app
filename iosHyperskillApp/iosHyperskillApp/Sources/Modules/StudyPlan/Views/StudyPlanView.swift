import shared
import SwiftUI

extension StudyPlanView {
    struct Appearance {
        let spacingBetweenContainers = LayoutInsets.largeInset
        let spacingBetweenRelativeItems = LayoutInsets.defaultInset

        let progressBlockSpacing = LayoutInsets.smallInset
        let progressBlockTitleInsets = LayoutInsets(bottom: LayoutInsets.smallInset)

        let backgroundColor = Color.systemGroupedBackground
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
        .navigationTitle(Strings.Track.title)
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
        switch viewModel.trackStateKs {
        case .idle:
            TrackSkeletonView()
                .onAppear {
                    viewModel.doLoadTrack()
                }
        case .loading:
            TrackSkeletonView()
        case .networkError:
            PlaceholderView(
                configuration: .networkError(backgroundColor: appearance.backgroundColor) {
                    viewModel.doLoadTrack(forceUpdate: true)
                }
            )
        case .content(let data):
            if data.isLoadingMagicLink {
                let _ = ProgressHUD.show()
            }

            let viewData = viewModel.makeViewData(
                track: data.track,
                trackProgress: data.trackProgress,
                studyPlan: data.studyPlan
            )

            ScrollView {
                VStack(spacing: appearance.spacingBetweenContainers) {
                    TrackHeaderView(
                        avatarSource: viewData.coverSource,
                        title: viewData.name,
                        subtitle: viewData.learningRole
                    )

                    if !viewModel.topicsToDiscoverNextStateKs.isEmpty {
                        TrackTopicsToDiscoverNextBlockView(
                            appearance: .init(spacing: appearance.spacingBetweenRelativeItems),
                            state: viewModel.topicsToDiscoverNextStateKs,
                            onTopicTapped: viewModel.doTheoryTopicPresentation(topicID:),
                            onErrorButtonTapped: viewModel.doReloadTopicsToDiscoverNext
                        )
                    }

                    TrackProgressBlockView(
                        appearance: .init(
                            titleInsets: appearance.progressBlockTitleInsets,
                            spacing: appearance.progressBlockSpacing
                        ),
                        timeToComplete: viewData.currentTimeToCompleteText,
                        completedGraduateProjects: viewData.completedGraduateProjectsCountText,
                        completedTopics: viewData.completedTopicsText,
                        completedTopicsProgress: viewData.completedTopicsProgress,
                        capstoneTopics: viewData.capstoneTopicsText,
                        capstoneTopicsProgress: viewData.capstoneTopicsProgress
                    )

                    TrackAboutBlockView(
                        appearance: .init(spacing: appearance.spacingBetweenRelativeItems),
                        rating: viewData.ratingText,
                        timeToComplete: viewData.allTimeToCompleteText,
                        projectsCount: viewData.projectsCountText,
                        topicsCount: viewData.topicsCountText,
                        description: viewData.description,
                        buttonText: viewData.webActionButtonText,
                        onButtonTapped: viewModel.doStudyPlanInWebPresentation
                    )
                }
                .padding(.vertical)
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

    private func handleViewAction(_ viewAction: StudyPlanActionViewAction) {

    }
}

struct StudyPlanView_Previews: PreviewProvider {
    static var previews: some View {
        UIKitViewControllerPreview {
            StudyPlanAssembly().makeModule()
        }
    }
}
