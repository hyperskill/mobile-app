import shared
import SwiftUI

extension ProjectSelectionListGridCellView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
        let textGroupSpacing = LayoutInsets.smallInset

        fileprivate func buttonStyle(isSelected: Bool) -> OutlineButtonStyle {
            OutlineButtonStyle(
                borderColor: isSelected ? Color(ColorPalette.overlayBlue) : .border,
                alignment: .leading,
                paddingEdgeSet: [],
                backgroundColor: Color(ColorPalette.surface)
            )
        }
    }
}

struct ProjectSelectionListGridCellView: View {
    private(set) var appearance = Appearance()

    let project: ProjectSelectionListFeature.ProjectListItem
    let isSelected: Bool

    let onTap: (Int64) -> Void

    var body: some View {
        Button(
            action: {
                onTap(project.id)
            },
            label: buildContent
        )
        .buttonStyle(appearance.buttonStyle(isSelected: isSelected))
    }

    @ViewBuilder
    private func buildContent() -> some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            ProjectSelectionListGridCellHeaderView(
                level: project.level.flatMap(SharedProjectLevelWrapper.init(sharedProjectLevel:)),
                isGraduate: project.isGraduate,
                averageRating: project.averageRating
            )

            VStack(alignment: .leading, spacing: appearance.textGroupSpacing) {
                Text(project.title)
                    .font(.body)
                    .foregroundColor(.primaryText)

                if let formattedTimeToComplete = project.formattedTimeToComplete {
                    Text(formattedTimeToComplete)
                        .font(.caption)
                        .foregroundColor(.secondaryText)
                }
            }

            ProjectSelectionListGridCellBadgesView(
                isSelected: isSelected,
                isIdeRequired: project.isIdeRequired,
                isBestRated: project.isBestRated,
                isFastestToComplete: project.isFastestToComplete
            )
        }
        .padding()
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
    }
}

struct ProjectSelectionListGridCellView_Previews: PreviewProvider {
    static var previews: some View {
        let project = ProjectSelectionListFeature.ProjectListItem(
            id: 1,
            title: "Simple Chatty Bot (Python)",
            averageRating: 4.7,
            level: .medium,
            formattedTimeToComplete: "24 hours",
            isGraduate: true,
            isBestRated: true,
            isIdeRequired: true,
            isFastestToComplete: true
        )

        VStack(spacing: LayoutInsets.defaultInset) {
            ProjectSelectionListGridCellView(
                project: project,
                isSelected: false,
                onTap: { _ in }
            )

            ProjectSelectionListGridCellView(
                project: project,
                isSelected: true,
                onTap: { _ in }
            )
        }
        .padding()
        .background(Color.systemGroupedBackground)

        VStack(spacing: LayoutInsets.defaultInset) {
            ProjectSelectionListGridCellView(
                project: project,
                isSelected: false,
                onTap: { _ in }
            )

            ProjectSelectionListGridCellView(
                project: project,
                isSelected: true,
                onTap: { _ in }
            )
        }
        .padding()
        .background(Color.systemGroupedBackground)
        .preferredColorScheme(.dark)
    }
}
