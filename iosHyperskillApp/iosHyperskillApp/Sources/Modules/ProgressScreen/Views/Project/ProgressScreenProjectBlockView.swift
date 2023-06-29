import SwiftUI

extension ProgressScreenProjectBlockView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
        let interitemSpacing = LayoutInsets.smallInset
    }
}

struct ProgressScreenProjectBlockView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            ProgressScreenProjectTitleView(
                avatarImageSource: "https://hyperskill.azureedge.net/media/tracks/9368deaab97441f192fd4c8db42cb9bc/python.svg",
                title: "Simple Chatty Bot"
            )

            HStack(spacing: appearance.interitemSpacing) {
                ProgressScreenTrackCardView(
                    title: "~ 56 h",
                    titleSecondaryText: nil,
                    imageName: Images.Step.clock,
                    progress: nil,
                    subtitle: "Time to complete the project"
                )

                ProgressScreenTrackCardView(
                    title: "0 / 5",
                    titleSecondaryText: nil,
                    imageName: "",
                    progress: .init(value: 0, isCompleted: false),
                    subtitle: "Stages"
                )
            }
        }
        .frame(maxWidth: .infinity)
    }
}

struct ProgressScreenProjectBlockView_Previews: PreviewProvider {
    static var previews: some View {
        ProgressScreenProjectBlockView()
            .padding()
            .background(Color.systemGroupedBackground)
    }
}
