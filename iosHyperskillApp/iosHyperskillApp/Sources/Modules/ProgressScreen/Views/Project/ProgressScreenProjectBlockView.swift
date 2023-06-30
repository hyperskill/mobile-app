import SwiftUI

extension ProgressScreenProjectBlockView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
        let interitemSpacing = LayoutInsets.smallInset
    }
}

struct ProgressScreenProjectBlockView: View {
    private(set) var appearance = Appearance()

    let projectLevel: SharedProjectLevelWrapper?

    let title: String

    let timeToCompleteLabel: String?

    let completedStagesLabel: String
    let completedStagesProgress: Float

    let isCompleted: Bool

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            ProgressScreenProjectTitleView(
                projectLevel: projectLevel,
                title: title
            )

            HStack(spacing: appearance.interitemSpacing) {
                if let timeToCompleteLabel {
                    ProgressScreenTrackCardView(
                        title: timeToCompleteLabel,
                        titleSecondaryText: nil,
                        imageName: Images.Step.clock,
                        progress: nil,
                        subtitle: "Time to complete the project"
                    )
                }

                ProgressScreenTrackCardView(
                    title: completedStagesLabel,
                    titleSecondaryText: nil,
                    imageName: "",
                    progress: .init(value: completedStagesProgress, isCompleted: isCompleted),
                    subtitle: "Stages"
                )
            }
        }
        .frame(maxWidth: .infinity)
    }
}

struct ProgressScreenProjectBlockView_Previews: PreviewProvider {
    static var previews: some View {
        ProgressScreenProjectBlockView(
            projectLevel: .easy,
            title: "Simple Chatty Bot",
            timeToCompleteLabel: "~ 56 h",
            completedStagesLabel: "0 / 5",
            completedStagesProgress: 0,
            isCompleted: false
        )
        .padding()
        .background(Color.systemGroupedBackground)
    }
}
