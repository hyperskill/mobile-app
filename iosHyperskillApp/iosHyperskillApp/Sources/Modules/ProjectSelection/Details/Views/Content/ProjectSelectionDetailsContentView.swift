import SwiftUI

extension ProjectSelectionDetailsContentView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
    }
}

struct ProjectSelectionDetailsContentView: View {
    private(set) var appearance = Appearance()

    let navigationTitle: String

    let learningOutcomesDescription: String?
    let isSelected: Bool
    let isIdeRequired: Bool
    let isBeta: Bool
    let isBestRated: Bool
    let isFastestToComplete: Bool
    let isBadgesVisible: Bool

    let averageRatingTitle: String
    let projectLevel: SharedProjectLevelWrapper?
    let projectLevelTitle: String?
    let graduateTitle: String?
    let timeToCompleteTitle: String?

    let providerName: String?

    let isCallToActionButtonEnabled: Bool
    let onCallToActionButtonTap: () -> Void

    var body: some View {
        ScrollView {
            VStack(spacing: appearance.spacing) {
                ProjectSelectionDetailsLearningOutcomesView(
                    description: learningOutcomesDescription,
                    isSelected: isSelected,
                    isIdeRequired: isIdeRequired,
                    isBeta: isBeta,
                    isBestRated: isBestRated,
                    isFastestToComplete: isFastestToComplete,
                    isBadgesVisible: isBadgesVisible
                )

                ProjectSelectionDetailsProjectOverviewView(
                    averageRatingTitle: averageRatingTitle,
                    projectLevel: projectLevel,
                    projectLevelTitle: projectLevelTitle,
                    graduateTitle: graduateTitle,
                    timeToCompleteTitle: timeToCompleteTitle
                )

                ProjectSelectionDetailsProviderView(title: providerName)
            }
            .padding()
        }
        .frame(maxWidth: .infinity)
        .navigationTitle(navigationTitle)
        .safeAreaInsetBottomCompatibility(footerView)
    }

    @MainActor @ViewBuilder private var footerView: some View {
        if isCallToActionButtonEnabled {
            Button(
                Strings.ProjectSelectionDetails.callToActionButtonTitle,
                action: {
                    FeedbackGenerator(feedbackType: .selection).triggerFeedback()
                    onCallToActionButtonTap()
                }
            )
            .buttonStyle(.primary)
            .shineEffect()
            .padding()
            .background(
                TransparentBlurView()
                    .edgesIgnoringSafeArea(.all)
            )
            .fixedSize(horizontal: false, vertical: true)
        } else {
            EmptyView()
        }
    }
}

#if DEBUG
struct ProjectSelectionDetailsContentView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            ZStack {
                BackgroundView(color: .systemGroupedBackground)

                ProjectSelectionDetailsContentView(
                    navigationTitle: "Words Virtuoso",
                    learningOutcomesDescription: """
This project is aimed at our beginners. \
It helps you understand some syntax basics and learn how to work with variables, data storage types such as lists, \
and while loops.
""",
                    isSelected: true,
                    isIdeRequired: true,
                    isBeta: true,
                    isBestRated: true,
                    isFastestToComplete: true,
                    isBadgesVisible: true,
                    averageRatingTitle: "It's new project, no rating yet",
                    projectLevel: .easy,
                    projectLevelTitle: "Easy project",
                    graduateTitle: "Graduate project. Solve at least one to complete the track.",
                    timeToCompleteTitle: "41 hours for project",
                    providerName: "JetBrains Academy",
                    isCallToActionButtonEnabled: true,
                    onCallToActionButtonTap: {}
                )
            }
        }
    }
}
#endif
