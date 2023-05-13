import shared
import SwiftUI

struct ProjectSelectionContentListView: View {
    let viewData: ProjectSelectionListFeatureViewStateContent

    let onProjectTap: (Int64) -> Void

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    private var projectsColumnsCount: Int {
        horizontalSizeClass == .regular ? 2 : 1
    }

    var body: some View {
        if let selectedProject = viewData.selectedProject {
            ProjectSelectionContentListSectionView(
                sectionTitle: nil,
                sectionSubtitle: nil,
                selectedProjectID: selectedProject.id,
                projects: [selectedProject],
                projectsColumnsCount: projectsColumnsCount,
                onProjectTap: onProjectTap
            )
        }

        // We can't iterate over a dictionary directly, so we need to convert it to an array first,
        // because it doen't conforms to 'RandomAccessCollection' protocol.
        let sortedProjectsLevels = viewData.projectsByLevel.keys
            .compactMap(SharedProjectLevelWrapper.init(sharedProjectLevel:))
            .sorted()

        ForEach(sortedProjectsLevels, id: \.self) { projectLevel in
            let projects = viewData.projectsByLevel[projectLevel.sharedProjectLevel] ?? []

            if projects.isEmpty {
                EmptyView()
            } else {
                ProjectSelectionContentListSectionView(
                    sectionTitle: projectLevel.title,
                    sectionSubtitle: projectLevel.description,
                    selectedProjectID: viewData.selectedProject?.id,
                    projects: projects,
                    projectsColumnsCount: projectsColumnsCount,
                    onProjectTap: onProjectTap
                )
            }
        }
    }
}

#if DEBUG
struct ProjectSelectionContentListView_Previews: PreviewProvider {
    static var previews: some View {
        ScrollView {
            VStack(spacing: ProjectSelectionContentView.Appearance().spacing) {
                ProjectSelectionContentListView(
                    viewData: .placeholder,
                    onProjectTap: { _ in }
                )
            }
            .padding()
        }
    }
}
#endif
