import SwiftUI

extension TrackSelectionListSkeletonView {
    struct Appearance {
        let listViewAppearance = TrackSelectionListView.Appearance()
        let gridViewAppearance = TrackSelectionListGridView.Appearance()

        let cellHeight: CGFloat = 107
    }
}

struct TrackSelectionListSkeletonView: View {
    private(set) var appearance = Appearance()

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    var body: some View {
        ScrollView([], showsIndicators: false) {
            VStack(spacing: appearance.listViewAppearance.spacing) {
                TrackSelectionListHeaderSkeletonView()

                let columnsCount = appearance.gridViewAppearance.tracksColumnsCount(for: horizontalSizeClass)

                LazyVGrid(
                    columns: .init(
                        repeating: .init(
                            .flexible(),
                            spacing: appearance.gridViewAppearance.interitemSpacing,
                            alignment: .top
                        ),
                        count: columnsCount
                    ),
                    alignment: .leading,
                    spacing: appearance.gridViewAppearance.interitemSpacing
                ) {
                    ForEach(0..<20) { _ in
                        SkeletonRoundedView()
                            .frame(height: appearance.cellHeight)
                    }
                }
            }
            .padding()
        }
        .frame(maxWidth: .infinity)
        .edgesIgnoringSafeArea(.bottom)
    }
}

struct TrackSelectionListSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            TrackSelectionListSkeletonView()
                .navigationTitle(Strings.TrackSelectionList.title)
        }
    }
}
