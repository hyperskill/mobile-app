import SwiftUI

extension TrackView {
    struct Appearance {
        let spacingBetweenContainers = LayoutInsets.largeInset
        let spacingBetweenRelativeItems = LayoutInsets.smallInset
    }
}

struct TrackView: View {
    private(set) var appearance = Appearance()

    let viewData: TrackViewData

    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: appearance.spacingBetweenContainers) {
                    TrackHeaderView(
                        iconImageName: viewData.iconImageName,
                        title: viewData.name,
                        subtitle: viewData.role
                    )

                    cardsView

                    TrackAboutView(
                        rating: viewData.rating,
                        timeToComplete: viewData.timeToComplete,
                        projectsCount: viewData.projectsCount,
                        topicsCount: viewData.topicsCount,
                        description: viewData.description,
                        buttonText: viewData.buttonText,
                        onButtonTapped: {}
                    )
                }
                .padding(.vertical)
            }
            .frame(maxWidth: .infinity)
            .navigationTitle(Strings.Track.title)
            .background(BackgroundView(color: .systemGroupedBackground))
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }

    private var cardsView: some View {
        VStack(spacing: appearance.spacingBetweenRelativeItems) {
            HStack(spacing: appearance.spacingBetweenRelativeItems) {
                TrackCardView(
                    title: viewData.timeToCompleteTrackTitle,
                    imageName: Images.Step.clock,
                    progress: nil,
                    subtitle: viewData.timeToCompleteTrackSubtitle
                )

                if let completedGraduateProjectTitle = viewData.completedGraduateProjectTitle {
                    TrackCardView(
                        title: completedGraduateProjectTitle,
                        imageName: Images.Track.projectGraduate,
                        imageRenderingMode: .original,
                        progress: nil,
                        subtitle: viewData.completedGraduateProjectSubtitle
                    )
                }
            }

            TrackCardView(
                title: viewData.completedTopicsTitle,
                imageName: Images.Track.About.topic,
                progress: viewData.completedTopicsProgress,
                subtitle: viewData.completedTopicsSubtitle
            )

            TrackCardView(
                title: viewData.appliedCoreTopicsByCompletingProjectStagesTitle,
                imageName: Images.Track.hammer,
                progress: viewData.appliedCoreTopicsByCompletingProjectStagesProgress,
                subtitle: viewData.appliedCoreTopicsByCompletingProjectStagesSubtitle
            )
        }
        .padding(.horizontal)
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
