import shared
import SwiftUI

extension TrackView {
    struct Appearance {
        let spacingBetweenContainers = LayoutInsets.largeInset
        let spacingBetweenRelativeItems = LayoutInsets.smallInset
    }
}

struct TrackView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: TrackViewModel

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: .systemGroupedBackground)

            buildBody()
        }
        .navigationTitle(Strings.Track.title)
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear(perform: viewModel.stopListening)
        .navigationViewStyle(StackNavigationViewStyle())
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        switch viewModel.state {
        case is TrackFeatureStateIdle:
            ProgressView()
                .onAppear {
                    viewModel.loadTrack()
                }
        case is TrackFeatureStateLoading:
            ProgressView()
        case is TrackFeatureStateNetworkError:
            PlaceholderView(
                configuration: .networkError {
                    viewModel.loadTrack(forceUpdate: true)
                }
            )
        case let content as TrackFeatureStateContent:
            let viewData = viewModel.makeViewData(
                track: content.track,
                trackProgress: content.trackProgress,
                studyPlan: content.studyPlan
            )

            ScrollView {
                VStack(spacing: appearance.spacingBetweenContainers) {
                    TrackHeaderView(
                        avatarSource: viewData.coverSource,
                        title: viewData.name,
                        subtitle: viewData.learningRole
                    )

                    TrackCardsView(
                        appearance: .init(spacing: appearance.spacingBetweenRelativeItems),
                        timeToComplete: viewData.currentTimeToCompleteText,
                        completedGraduateProjects: viewData.completedGraduateProjectsCountText,
                        completedTopics: viewData.completedTopicsText,
                        completedTopicsProgress: viewData.completedTopicsProgress,
                        capstoneTopics: viewData.capstoneTopicsText,
                        capstoneTopicsProgress: viewData.capstoneTopicsProgress
                    )

                    TrackAboutView(
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
            }
            .frame(maxWidth: .infinity)
        default:
            Text("Unkwown state")
        }
    }

    private func handleViewAction(_ viewAction: TrackFeatureActionViewAction) {
        print("TrackView :: \(#function) viewAction = \(viewAction)")
    }
}

struct TrackView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            TrackAssembly().makeModule()

            TrackAssembly()
                .makeModule()
                .preferredColorScheme(.dark)
        }
    }
}
