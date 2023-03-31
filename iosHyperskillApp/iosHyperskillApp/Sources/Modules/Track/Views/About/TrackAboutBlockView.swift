import SwiftUI

extension TrackAboutBlockView {
    struct Appearance {
        let insets = LayoutInsets(horizontal: LayoutInsets.defaultInset, vertical: LayoutInsets.largeInset)

        var spacing = LayoutInsets.defaultInset
    }
}

struct TrackAboutBlockView: View {
    private(set) var appearance = Appearance()

    let rating: String?
    let timeToComplete: String?
    let projectsCount: String?
    let topicsCount: String?

    let description: String

    let buttonText: String
    var onButtonTapped: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            Text(Strings.Track.About.title)
                .font(.title3)
                .foregroundColor(.primaryText)
                .bold()
                .frame(maxWidth: .infinity, alignment: .leading)

            TrackAboutStatsView(
                rating: rating,
                timeToComplete: timeToComplete,
                project: projectsCount,
                topic: topicsCount
            )

            Text(description)
                .font(.callout)
                .foregroundColor(.primaryText)

            Button(action: onButtonTapped, label: { Text(buttonText).underline() })
                .font(.body)
                .foregroundColor(.secondaryText)
        }
        .frame(maxWidth: .infinity)
        .padding(appearance.insets.edgeInsets)
        .background(BackgroundView(color: Color(ColorPalette.surface)))
    }
}

struct TrackAboutBlockView_Previews: PreviewProvider {
    static var previews: some View {
        TrackAboutBlockView(
            rating: "4.7",
            timeToComplete: "104 hours",
            projectsCount: "20 projects",
            topicsCount: "220 topics",
            description: """
You've never tried programming and would like to build a solid foundation? This track is perfect for you! While
completing this track you’ll create 5 simple projects. We’ll start with the basics and will use a lot of examples to
really explain the possibilities of...
""",
            buttonText: "Keep your progress in web ↗",
            onButtonTapped: {}
        )
        .previewLayout(.sizeThatFits)
    }
}
