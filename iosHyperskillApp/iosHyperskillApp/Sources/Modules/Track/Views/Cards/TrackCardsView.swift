import SwiftUI

extension TrackCardsView {
    struct Appearance {
        var spacing: CGFloat = LayoutInsets.defaultInset
    }
}

struct TrackCardsView: View {
    private(set) var appearance = Appearance()

    let timeToComplete: String?

    let completedGraduateProjects: String?

    let completedTopics: String?
    let completedTopicsProgress: Float

    let capstoneTopics: String?
    let capstoneTopicsProgress: Float

    private var shouldDisplayCards: Bool {
        timeToComplete != nil || completedGraduateProjects != nil || completedTopics != nil || capstoneTopics != nil
    }

    var body: some View {
        if shouldDisplayCards {
            VStack(spacing: appearance.spacing) {
                if timeToComplete != nil || completedGraduateProjects != nil {
                    HStack(spacing: appearance.spacing) {
                        if let currentTimeToCompleteText = timeToComplete {
                            TrackCardView(
                                title: currentTimeToCompleteText,
                                imageName: Images.Step.clock,
                                progress: nil,
                                subtitle: Strings.Track.timeToComplete
                            )
                        }

                        if let completedGraduateProjectsCountText = completedGraduateProjects {
                            TrackCardView(
                                title: completedGraduateProjectsCountText,
                                imageName: Images.Track.projectGraduate,
                                imageRenderingMode: .original,
                                progress: nil,
                                subtitle: Strings.Track.completedGraduateProject
                            )
                        }
                    }
                }

                if let completedTopicsText = completedTopics {
                    TrackCardView(
                        title: completedTopicsText,
                        imageName: Images.Track.About.topic,
                        progress: completedTopicsProgress,
                        subtitle: Strings.Track.completedTopics
                    )
                }

                if let capstoneTopicsText = capstoneTopics {
                    TrackCardView(
                        title: capstoneTopicsText,
                        imageName: Images.Track.hammer,
                        progress: capstoneTopicsProgress,
                        subtitle: Strings.Track.appliedCoreTopics
                    )
                }
            }
            .padding(.horizontal)
        } else {
            EmptyView()
        }
    }
}

struct TrackCardsView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            TrackCardsView(
                timeToComplete: nil,
                completedGraduateProjects: nil,
                completedTopics: nil,
                completedTopicsProgress: 0,
                capstoneTopics: nil,
                capstoneTopicsProgress: 0
            )

            TrackCardsView(
                timeToComplete: "test",
                completedGraduateProjects: "test",
                completedTopics: "test",
                completedTopicsProgress: 0,
                capstoneTopics: "test",
                capstoneTopicsProgress: 0
            )
        }
        .previewLayout(.sizeThatFits)
    }
}
