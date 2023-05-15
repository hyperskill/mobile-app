import Foundation

struct TrackSelectionListItem {
    let id: Int64

    let imageSource: String?

    let title: String

    let timeToComplete: String?
    let rating: String

    let isSelected: Bool
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
            isSelected: false,
            isIdeRequired: true,
            isBeta: true,
            isCompleted: false
        )
    }

    static var placeholderSelected: TrackSelectionListItem {
        TrackSelectionListItem(
            id: 0,
            imageSource: "https://hyperskill.azureedge.net/media/tracks/9368deaab97441f192fd4c8db42cb9bc/python.svg",
            title: "Python Core",
            timeToComplete: "136 hours",
            rating: "4.7",
            isSelected: true,
            isIdeRequired: true,
            isBeta: true,
            isCompleted: false
        )
    }

    static var placeholders: [TrackSelectionListItem] {
        (0..<10).map { index in
            let rating: String = {
                let value = Double.random(in: 0...5.0)
                return Formatter.averageRating(value, decimalPoints: 1)
            }()

            let timeToComplete: String = {
                let hours = Int.random(in: 1...100)
                return Formatter(
                    resourceProvider: AppGraphBridge.sharedAppGraph.commonComponent.resourceProvider
                ).hoursCount(hours)
            }()

            return TrackSelectionListItem(
                id: Int64(index + 1),
                imageSource: "https://hyperskill.azureedge.net/media/tracks/9368deaab97441f192fd4c8db42cb9bc/python.svg",
                title: "Python Core",
                timeToComplete: timeToComplete,
                rating: rating,
                isSelected: false,
                isIdeRequired: index.isMultiple(of: 3),
                isBeta: index.isMultiple(of: 3),
                isCompleted: index == 2
            )
        }
    }
}
