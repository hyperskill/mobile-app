import SwiftUI

extension ProfileStatisticsItemView {
    struct Appearance {
        let iconWidthHeight: CGFloat = 28
        let iconWithCircleBackgroundImageWidthHeight: CGFloat = 16

        var cornerRadius: CGFloat = 8
    }
}

struct ProfileStatisticsItemView: View {
    private(set) var appearance = Appearance()

    let icon: Icon

    let title: String

    let subtitle: String

    var body: some View {
        VStack {
            HStack {
                image

                Text(title)
                    .font(.subheadline)
                    .foregroundColor(.primaryText)
            }

            Text(subtitle)
                .font(.caption)
                .foregroundColor(.disabledText)
        }
        .frame(maxWidth: .infinity)
        .padding()
        .background(Color(ColorPalette.surface))
        .cornerRadius(appearance.cornerRadius)
    }

    @ViewBuilder
    private var image: some View {
        switch icon.renderingMode {
        case .original:
            Image(icon.imageName)
                .resizable()
                .renderingMode(.original)
                .aspectRatio(contentMode: .fit)
                .frame(widthHeight: appearance.iconWidthHeight)
        case .circleBackground:
            Image(icon.imageName)
                .resizable()
                .renderingMode(.template)
                .aspectRatio(contentMode: .fit)
                .frame(widthHeight: appearance.iconWithCircleBackgroundImageWidthHeight)
                .foregroundColor(Color(ColorPalette.onSurfaceAlpha60))
                .frame(widthHeight: appearance.iconWidthHeight)
                .background(Color.background)
                .clipShape(Circle())
        }
    }

    struct Icon {
        let imageName: String
        let renderingMode: RenderingMode

        enum RenderingMode {
            case original
            case circleBackground
        }
    }
}

struct ProfileStatisticsItemView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ProfileStatisticsItemView(
                icon: .init(imageName: Images.Track.About.project, renderingMode: .circleBackground),
                title: "3",
                subtitle: Strings.Profile.Statistics.passedProjects
            )

            ProfileStatisticsItemView(
                icon: .init(imageName: Images.TabBar.track, renderingMode: .circleBackground),
                title: "3",
                subtitle: Strings.Profile.Statistics.passedTracks
            )

            ProfileStatisticsItemView(
                icon: .init(imageName: Images.StepQuiz.ProblemOfDaySolvedModal.gemsBadge, renderingMode: .original),
                title: "3456",
                subtitle: Strings.Profile.Statistics.hypercoinsBalance
            )
        }
        .padding()
        .background(Color.background)
    }
}
