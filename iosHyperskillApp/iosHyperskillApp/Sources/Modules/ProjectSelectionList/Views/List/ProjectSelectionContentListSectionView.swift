import shared
import SwiftUI

extension ProjectSelectionContentListSectionView {
    struct Appearance {
        let rootSpacing: CGFloat = 16
        let interitemSpacing: CGFloat = 8
    }
}

struct ProjectSelectionContentListSectionView: View {
    private(set) var appearance = Appearance()

    let sectionTitle: String?
    let sectionSubtitle: String?

    let selectedProjectID: Int64?

    let projects: [ProjectSelectionListFeature.ProjectListItem]
    let projectsColumnsCount: Int

    let onProjectTap: (Int64) -> Void

    var body: some View {
        if projects.isEmpty {
            EmptyView()
        } else {
            VStack(alignment: .leading, spacing: appearance.rootSpacing) {
                buildSectionHeader()
                buildSectionContent()
            }
        }
    }

    @ViewBuilder
    private func buildSectionHeader() -> some View {
        if sectionTitle?.isEmpty ?? true && sectionSubtitle?.isEmpty ?? true {
            EmptyView()
        } else {
            VStack(alignment: .leading, spacing: appearance.interitemSpacing) {
                if let sectionTitle = sectionTitle {
                    Text(sectionTitle)
                        .font(.title3)
                        .bold()
                        .foregroundColor(.primaryText)
                }

                if let sectionSubtitle = sectionSubtitle {
                    Text(sectionSubtitle)
                        .font(.callout)
                        .foregroundColor(.secondaryText)
                }
            }
        }
    }

    @ViewBuilder
    private func buildSectionContent() -> some View {
        LazyVGrid(
            columns: .init(
                repeating: .init(
                    .flexible(),
                    spacing: appearance.interitemSpacing,
                    alignment: .top
                ),
                count: projectsColumnsCount
            ),
            alignment: .leading,
            spacing: appearance.interitemSpacing
        ) {
            ForEach(projects, id: \.id) { project in
                ProjectSelectionListCellView(
                    project: project,
                    isSelected: project.id == selectedProjectID,
                    onTap: onProjectTap
                )
            }
        }
    }
}

#if DEBUG
struct ProjectSelectionContentListSectionView_Previews: PreviewProvider {
    static var previews: some View {
        let placeholder = ProjectSelectionListFeatureViewStateContent.placeholder

        ProjectSelectionContentListSectionView(
            sectionTitle: Strings.ProjectSelectionList.List.Category.easyTitle,
            sectionSubtitle: Strings.ProjectSelectionList.List.Category.easyDescription,
            selectedProjectID: 1,
            projects: placeholder.projectsByLevel[.easy] ?? [],
            projectsColumnsCount: 4,
            onProjectTap: { _ in }
        )
        .padding()
        .background(Color.systemGroupedBackground)

        ProjectSelectionContentListSectionView(
            sectionTitle: Strings.ProjectSelectionList.List.Category.mediumTitle,
            sectionSubtitle: Strings.ProjectSelectionList.List.Category.mediumDescription,
            selectedProjectID: 1,
            projects: placeholder.projectsByLevel[.medium] ?? [],
            projectsColumnsCount: 7,
            onProjectTap: { _ in }
        )
        .padding()
        .background(Color.systemGroupedBackground)
    }
}
#endif
