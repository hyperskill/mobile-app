import SwiftUI

extension ProjectSelectionListGridSectionSkeletonView {
    struct Appearance {
        let gridSectionViewAppearance = ProjectSelectionListGridSectionView.Appearance()

        let defaultProjectCellHeight: CGFloat = 107
        let selectedProjectCellHeight: CGFloat = 146

        let sectionTitleHeight: CGFloat = 24
    }
}

struct ProjectSelectionListGridSectionSkeletonView: View {
    private(set) var appearance = Appearance()

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    private var projectsColumnsCount: Int {
        horizontalSizeClass == .regular ? 2 : 1
    }

    var body: some View {
        SkeletonRoundedView()
            .frame(height: appearance.selectedProjectCellHeight)

        ForEach(0..<4) { _ in
            VStack(spacing: appearance.gridSectionViewAppearance.rootSpacing) {
                SkeletonRoundedView()
                    .frame(height: appearance.sectionTitleHeight)

                LazyVGrid(
                    columns: .init(
                        repeating: .init(
                            .flexible(),
                            spacing: appearance.gridSectionViewAppearance.interitemSpacing,
                            alignment: .top
                        ),
                        count: projectsColumnsCount
                    ),
                    alignment: .leading,
                    spacing: appearance.gridSectionViewAppearance.interitemSpacing
                ) {
                    ForEach(0..<2) { _ in
                        SkeletonRoundedView()
                            .frame(height: appearance.defaultProjectCellHeight)
                    }
                }
            }
        }
    }
}

struct ProjectSelectionListGridSectionSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: ProjectSelectionListSkeletonView.Appearance().listViewAppearance.spacing) {
            ProjectSelectionListGridSectionSkeletonView()
        }
        .padding()
    }
}
