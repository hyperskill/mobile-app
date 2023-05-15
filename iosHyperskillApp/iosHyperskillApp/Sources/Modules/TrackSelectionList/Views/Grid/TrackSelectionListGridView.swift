import SwiftUI

extension TrackSelectionListGridView {
    struct Appearance {
        let defaultColumnsCount = 1
        let regularHorizontalSizeClassColumnsCount = 2

        let interitemSpacing: CGFloat = 8

        func tracksColumnsCount(for horizontalSizeClass: UserInterfaceSizeClass?) -> Int {
            horizontalSizeClass == .regular ? regularHorizontalSizeClassColumnsCount : defaultColumnsCount
        }
    }
}

struct TrackSelectionListGridView: View {
    private(set) var appearance = Appearance()

    let selectedTrack: TrackSelectionListItem?
    let tracks: [TrackSelectionListItem]

    let onTrackTap: (Int64) -> Void

    private let trackSelectionFeedbackGenerator = FeedbackGenerator(feedbackType: .selection)

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    private var resultTracks: [TrackSelectionListItem] {
        if let selectedTrack {
            return [selectedTrack] + tracks
        }
        return tracks
    }

    var body: some View {
        let columnsCount = appearance.tracksColumnsCount(for: horizontalSizeClass)

        if resultTracks.isEmpty {
            EmptyView()
        } else {
            LazyVGrid(
                columns: Array(
                    repeating: GridItem(
                        .flexible(),
                        spacing: appearance.interitemSpacing,
                        alignment: .top
                    ),
                    count: columnsCount
                ),
                alignment: .leading,
                spacing: appearance.interitemSpacing
            ) {
                ForEach(resultTracks, id: \.id) { track in
                    TrackSelectionListGridCellView(
                        track: track,
                        isSelected: track.isSelected,
                        onTap: { handleTrackTapped(trackID: $0) }
                    )
                }
            }
        }
    }

    @MainActor
    private func handleTrackTapped(trackID: Int64) {
        trackSelectionFeedbackGenerator.triggerFeedback()
        onTrackTap(trackID)
    }
}

struct TrackSelectionListGridView_Previews: PreviewProvider {
    static var previews: some View {
        ScrollView {
            TrackSelectionListGridView(
                selectedTrack: .placeholderSelected,
                tracks: TrackSelectionListItem.placeholders,
                onTrackTap: { _ in }
            )
            .padding()
        }
    }
}
