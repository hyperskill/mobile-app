import shared
import SwiftUI

extension ProjectSelectionListGridView {
    struct Appearance {
        let defaultColumnsCount = 1
        let regularHorizontalSizeClassColumnsCount = 2

        func projectsColumnsCount(for horizontalSizeClass: UserInterfaceSizeClass?) -> Int {
            horizontalSizeClass == .regular ? regularHorizontalSizeClassColumnsCount : defaultColumnsCount
        }
    }
}

struct ProjectSelectionListGridView: View {
    private(set) var appearance = Appearance()

    let viewData: ProjectSelectionListFeatureViewStateContent

    let onProjectTap: (Int64) -> Void

    private let projectSelectionFeedbackGenerator = FeedbackGenerator(feedbackType: .selection)

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    var body: some View {
        let projectsColumnsCount = appearance.projectsColumnsCount(for: horizontalSizeClass)

        if let selectedProject = viewData.selectedProject {
            ProjectSelectionListGridSectionView(
                sectionTitle: nil,
                sectionSubtitle: nil,
                selectedProjectID: selectedProject.id,
                projects: [selectedProject],
                projectsColumnsCount: projectsColumnsCount,
                onProjectTap: { handleProjectTapped(projectID: $0) }
            )
        }

        ProjectSelectionListGridSectionView(
            sectionTitle: Strings.ProjectSelectionList.List.recommendedProjectsTitle,
            sectionSubtitle: nil,
            selectedProjectID: nil,
            projects: viewData.recommendedProjects,
            projectsColumnsCount: projectsColumnsCount,
            onProjectTap: { handleProjectTapped(projectID: $0) }
        )

        // We can't iterate over a dictionary directly, so we need to convert it to an array first,
        // because it doen't conforms to 'RandomAccessCollection' protocol.
        let sortedProjectsByLevel = viewData.projectsByLevel.keys
            .compactMap(SharedProjectLevelWrapper.init(sharedProjectLevel:))
            .sorted()

        ForEach(sortedProjectsByLevel, id: \.self) { projectLevel in
            let projects = viewData.projectsByLevel[projectLevel.sharedProjectLevel] ?? []

            if projects.isEmpty {
                EmptyView()
            } else {
                ProjectSelectionListGridSectionView(
                    sectionTitle: projectLevel.title,
                    sectionSubtitle: projectLevel.description,
                    selectedProjectID: nil,
                    projects: projects,
                    projectsColumnsCount: projectsColumnsCount,
                    onProjectTap: { handleProjectTapped(projectID: $0) }
                )
            }
        }
    }

    @MainActor
    private func handleProjectTapped(projectID: Int64) {
        projectSelectionFeedbackGenerator.triggerFeedback()
        onProjectTap(projectID)
    }
}

#if DEBUG
struct ProjectSelectionListGridView_Previews: PreviewProvider {
    static var previews: some View {
        ScrollView {
            VStack(spacing: ProjectSelectionListView.Appearance().spacing) {
                ProjectSelectionListGridView(
                    viewData: .placeholder,
                    onProjectTap: { _ in }
                )
            }
            .padding()
        }
    }
}
#endif
