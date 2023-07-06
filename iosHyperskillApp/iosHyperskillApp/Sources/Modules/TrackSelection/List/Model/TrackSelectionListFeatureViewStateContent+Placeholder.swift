import Foundation
import shared

#if DEBUG
extension TrackSelectionListFeatureViewStateContent {
    static var placeholder: TrackSelectionListFeatureViewStateContent {
        let tracks = (0..<10).map { index in
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

            return TrackSelectionListFeatureViewStateTrack(
                id: Int64(index + 1),
                imageSource: "https://hyperskill.azureedge.net/media/tracks/9368deaab97441f192fd4c8db42cb9bc/python.svg",
                title: "Python Core",
                timeToComplete: timeToComplete,
                rating: rating,
                isBeta: index.isMultiple(of: 3),
                isCompleted: index == 2,
                isSelected: index == 0,
                progress: Int32.random(in: 0...100)
            )
        }

        return TrackSelectionListFeatureViewStateContent(
            tracks: tracks
        )
    }
}
#endif
