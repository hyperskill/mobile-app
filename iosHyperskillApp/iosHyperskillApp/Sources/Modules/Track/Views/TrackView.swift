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

    @ObservedObject private var viewModel: TrackViewModel

    init(viewModel: TrackViewModel) {
        self.viewModel = viewModel
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        NavigationView {
            ZStack {
                BackgroundView(color: .systemGroupedBackground)
                buildBody()
            }
            .navigationTitle(Strings.Track.title)
        }
        .navigationViewStyle(StackNavigationViewStyle())
        .onAppear(perform: viewModel.startListening)
        .onDisappear(perform: viewModel.stopListening)
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

                    cardsView(viewData: viewData)

                    TrackAboutView(
                        rating: viewData.ratingText,
                        timeToComplete: viewData.allTimeToCompleteText,
                        projectsCount: viewData.projectsCountText,
                        topicsCount: viewData.topicsCountText,
                        description: viewData.description,
                        buttonText: viewData.webActionButtonText,
                        onButtonTapped: {}
                    )
                }
                .padding(.vertical)
            }
            .frame(maxWidth: .infinity)
        default:
            Text("Unkwown state")
        }
    }

    private func cardsView(viewData: TrackViewData) -> some View {
        VStack(spacing: appearance.spacingBetweenRelativeItems) {
            HStack(spacing: appearance.spacingBetweenRelativeItems) {
                TrackCardView(
                    title: viewData.currentTimeToCompleteText,
                    imageName: Images.Step.clock,
                    progress: nil,
                    subtitle: Strings.Track.timeToComplete
                )

                if let completedGraduateProjectsCountText = viewData.completedGraduateProjectsCountText {
                    TrackCardView(
                        title: completedGraduateProjectsCountText,
                        imageName: Images.Track.projectGraduate,
                        imageRenderingMode: .original,
                        progress: nil,
                        subtitle: Strings.Track.completedGraduateProject
                    )
                }
            }

            TrackCardView(
                title: viewData.completedTopicsText,
                imageName: Images.Track.About.topic,
                progress: viewData.completedTopicsProgress,
                subtitle: Strings.Track.completedTopics
            )

            if let capstoneTopicsText = viewData.capstoneTopicsText {
                TrackCardView(
                    title: capstoneTopicsText,
                    imageName: Images.Track.hammer,
                    progress: viewData.capstoneTopicsProgress,
                    subtitle: Strings.Track.appliedCoreTopics
                )
            }
        }
        .padding(.horizontal)
    }

    private func handleViewAction(_ viewAction: TrackFeatureActionViewAction) {
        print("TrackView :: \(#function) viewAction = \(viewAction)")
    }
}

struct TrackView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            TrackAssembly(trackID: 2).makeModule()

            TrackAssembly(trackID: 2)
                .makeModule()
                .preferredColorScheme(.dark)
        }
    }
}
