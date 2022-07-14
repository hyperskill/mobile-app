import SwiftUI

extension TrackAboutStatsView {
    struct Appearance {
        let spacing: CGFloat = 12
    }
}

struct TrackAboutStatsView: View {
    private(set) var appearance = Appearance()

    let rating: String?
    let timeToComplete: String?
    let project: String?
    let topic: String?

    var body: some View {
        HStack(spacing: appearance.spacing) {
            if let rating = rating {
                TrackAboutStatItemView(itemType: .rating, text: rating)
            }

            if let timeToComplete = timeToComplete {
                TrackAboutStatItemView(itemType: .timeToComplete, text: timeToComplete)
            }

            if let project = project {
                TrackAboutStatItemView(itemType: .project, text: project)
            }

            if let topic = topic {
                TrackAboutStatItemView(itemType: .topic, text: topic)
            }
        }
    }
}

struct TrackAboutStatsView_Previews: PreviewProvider {
    static var previews: some View {
        TrackAboutStatsView(
            rating: "4.7",
            timeToComplete: "104 hours",
            project: "20 projects",
            topic: "220 topics"
        )
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
