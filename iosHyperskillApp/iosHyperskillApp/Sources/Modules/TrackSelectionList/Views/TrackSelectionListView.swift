import SwiftUI

extension TrackSelectionListView {
    struct Appearance {
        let spacing: CGFloat = 32

        let backgroundColor = Color.systemGroupedBackground
    }
}

struct TrackSelectionListView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        ZStack {
            BackgroundView(color: appearance.backgroundColor)

            buildBody()
        }
        .navigationBarTitleDisplayMode(.inline)
        .navigationViewStyle(StackNavigationViewStyle())
    }

    // MARK: Private API

    @ViewBuilder
    private func buildBody() -> some View {
        ScrollView {
            VStack(spacing: appearance.spacing) {
                TrackSelectionListHeaderView()
                    .padding(.top, appearance.spacing)

                TrackSelectionListGridView(
                    selectedTrack: .placeholderSelected,
                    tracks: TrackSelectionListItem.placeholders,
                    onTrackTap: { _ in }
                )
            }
            .padding([.horizontal, .bottom])
        }
        .frame(maxWidth: .infinity)
    }
}

struct TrackSelectionListView_Previews: PreviewProvider {
    static var previews: some View {
        TrackSelectionListView()
    }
}
