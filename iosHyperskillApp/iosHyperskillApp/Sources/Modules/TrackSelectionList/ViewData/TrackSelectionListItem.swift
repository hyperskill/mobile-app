import Foundation

struct TrackSelectionListItem {
    let id: Int64

    let imageSource: String?

    let title: String

    let timeToComplete: String?
    let rating: String

    let isIdeRequired: Bool
    let isBeta: Bool
    let isCompleted: Bool
}

extension TrackSelectionListItem {
    static var placeholder: TrackSelectionListItem {
        TrackSelectionListItem(
            id: 0,
            imageSource: "https://hyperskill.azureedge.net/media/tracks/9368deaab97441f192fd4c8db42cb9bc/python.svg",
            title: "Python Core",
            timeToComplete: "136 hours",
            rating: "4.7",
            isIdeRequired: true,
            isBeta: true,
            isCompleted: false
        )
    }
}
