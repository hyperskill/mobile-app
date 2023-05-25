import shared
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

    let tracks: [TrackSelectionListFeatureViewStateTrack]

    let onTrackTap: (Int64) -> Void

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    var body: some View {
        let columnsCount = appearance.tracksColumnsCount(for: horizontalSizeClass)

        if tracks.isEmpty {
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
                ForEach(tracks, id: \.id) { track in
                    TrackSelectionListGridCellView(
                        track: track,
                        onTap: onTrackTap
                    )
                }
            }
        }
    }
}

#if DEBUG
struct TrackSelectionListGridView_Previews: PreviewProvider {
    static var previews: some View {
        ScrollView {
            TrackSelectionListGridView(
                tracks: TrackSelectionListFeatureViewStateContent.placeholder.tracks,
                onTrackTap: { _ in }
            )
            .padding()
        }
        .background(BackgroundView(color: .systemGroupedBackground))
    }
}
#endif
