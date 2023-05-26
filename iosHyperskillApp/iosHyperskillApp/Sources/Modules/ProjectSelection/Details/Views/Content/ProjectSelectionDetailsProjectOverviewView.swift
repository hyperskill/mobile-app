import SwiftUI

extension ProjectSelectionDetailsProjectOverviewView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
        let interitemSpacing = LayoutInsets.smallInset

        let itemFont = Font.body
        let itemForegroundColor = Color.primaryText
        let itemImageSize = CGSize(width: 16, height: 16)

        func makeRatingViewAppearance() -> StarRatingView.Appearance {
            StarRatingView.Appearance(
                spacing: interitemSpacing,
                imageSize: itemImageSize,
                imageColor: Color(ColorPalette.overlayYellow),
                textFont: itemFont,
                textColor: itemForegroundColor
            )
        }
    }
}

struct ProjectSelectionDetailsProjectOverviewView: View {
    private(set) var appearance = Appearance()

    let averageRatingTitle: String

    let projectLevel: SharedProjectLevelWrapper?
    let projectLevelTitle: String?

    let graduateTitle: String?

    let timeToCompleteTitle: String?

    private var isEmpty: Bool {
        averageRatingTitle.isEmpty
        && (projectLevel == nil || projectLevelTitle?.isEmpty ?? true)
        && (graduateTitle?.isEmpty ?? true)
        && (timeToCompleteTitle?.isEmpty ?? true)
    }

    var body: some View {
        if isEmpty {
            EmptyView()
        } else {
            CardView {
                VStack(alignment: .leading, spacing: appearance.spacing) {
                    Text(Strings.ProjectSelectionDetails.projectOverviewTitle)
                        .font(.headline)

                    VStack(alignment: .leading, spacing: appearance.spacing) {
                        if !averageRatingTitle.isEmpty {
                            StarRatingView(
                                appearance: appearance.makeRatingViewAppearance(),
                                rating: .string(averageRatingTitle)
                            )
                        }

                        buildItemView(
                            title: projectLevelTitle,
                            iconImageName: projectLevel?.iconImageName,
                            iconImageRenderingMode: .original
                        )
                        buildItemView(
                            title: graduateTitle,
                            iconImageName: Images.ProjectSelectionList.projectGraduate
                        )
                        buildItemView(title: timeToCompleteTitle, iconImageName: Images.Step.clock)
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
        }
    }

    @ViewBuilder
    private func buildItemView(
        title: String?,
        iconImageName: String?,
        iconImageRenderingMode: Image.TemplateRenderingMode = .template
    ) -> some View {
        if let title, !title.isEmpty,
           let iconImageName, !iconImageName.isEmpty {
            Label(
                title: {
                    Text(title)
                        .font(appearance.itemFont)
                        .foregroundColor(appearance.itemForegroundColor)
                },
                icon: {
                    Image(iconImageName)
                        .resizable()
                        .renderingMode(iconImageRenderingMode)
                        .aspectRatio(contentMode: .fill)
                        .frame(size: appearance.itemImageSize, alignment: .center)
                        .foregroundColor(appearance.itemForegroundColor)
                }
            )
        }
    }
}

#if DEBUG
struct ProjectSelectionDetailsProjectOverviewView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectSelectionDetailsProjectOverviewView(
            averageRatingTitle: "It's new project, no rating yet",
            projectLevel: .easy,
            projectLevelTitle: "Easy project",
            graduateTitle: "Graduate project. Solve at least one to complete the track.",
            timeToCompleteTitle: "41 hours for project"
        )
        .padding()
        .background(Color.systemGroupedBackground)
    }
}
#endif
