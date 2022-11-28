import SwiftUI

extension TrackProgressBlockView {
    struct Appearance {
        let insets = LayoutInsets(horizontal: LayoutInsets.defaultInset)

        var titleInsets = LayoutInsets(bottom: LayoutInsets.smallInset)

        var spacing = LayoutInsets.defaultInset
    }
}

struct TrackProgressBlockView: View {
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
            VStack(alignment: .leading, spacing: appearance.spacing) {
                Text(Strings.Track.Progress.title)
                    .font(.title3)
                    .foregroundColor(.primaryText)
                    .bold()
                    .padding(appearance.titleInsets.edgeInsets)

                cardsView
            }
            .frame(maxWidth: .infinity)
            .padding(appearance.insets.edgeInsets)
        } else {
            EmptyView()
        }
    }

    private var cardsView: some View {
        VStack(spacing: appearance.spacing) {
            if timeToComplete != nil || completedGraduateProjects != nil {
                HStack(spacing: appearance.spacing) {
                    if let currentTimeToCompleteText = timeToComplete {
                        TrackProgressCardView(
                            title: currentTimeToCompleteText,
                            imageName: Images.Step.clock,
                            progress: nil,
                            subtitle: Strings.Track.Progress.timeToComplete
                        )
                    }

                    if let completedGraduateProjectsCountText = completedGraduateProjects {
                        TrackProgressCardView(
                            title: completedGraduateProjectsCountText,
                            imageName: Images.Track.projectGraduate,
                            imageRenderingMode: .original,
                            progress: nil,
                            subtitle: Strings.Track.Progress.completedGraduateProject
                        )
                    }
                }
            }

            if let completedTopicsText = completedTopics {
                TrackProgressCardView(
                    title: completedTopicsText,
                    imageName: Images.Track.About.topic,
                    progress: completedTopicsProgress,
                    subtitle: Strings.Track.Progress.completedTopics
                )
            }

            if let capstoneTopicsText = capstoneTopics {
                TrackProgressCardView(
                    title: capstoneTopicsText,
                    imageName: Images.Track.hammer,
                    progress: capstoneTopicsProgress,
                    subtitle: Strings.Track.Progress.appliedCoreTopics
                )
            }
        }
    }
}

struct TrackProgressBlockView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            TrackProgressBlockView(
                timeToComplete: nil,
                completedGraduateProjects: nil,
                completedTopics: nil,
                completedTopicsProgress: 0,
                capstoneTopics: nil,
                capstoneTopicsProgress: 0
            )

            TrackProgressBlockView(
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
