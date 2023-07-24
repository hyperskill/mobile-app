import shared
import SwiftUI

extension NextLearningActivityView {
    struct Appearance {
        var spacing = LayoutInsets.largeInset
    }
}

struct NextLearningActivityView: View {
    private(set) var appearance = Appearance()

    let stateKs: NextLearningActivityWidgetFeatureViewStateKs

    let onActivityTap: () -> Void

    let onReloadButtonTap: () -> Void

    var body: some View {
        switch stateKs {
        case .idle, .loading:
            NextLearningActivitySkeletonView()
        case .networkError:
            Button(
                Strings.Placeholder.networkErrorButtonText,
                action: onReloadButtonTap
            )
            .buttonStyle(OutlineButtonStyle())
        case .empty:
            EmptyView()
        case .content(let content):
            VStack(alignment: .leading, spacing: appearance.spacing) {
                Text(Strings.Home.nextLearningActivityTitle)
                    .font(.subheadline)
                    .foregroundColor(.secondaryText)

                StudyPlanSectionItemView(
                    title: content.title,
                    subtitle: content.subtitle,
                    isClickable: true,
                    progress: content.progress,
                    formattedProgress: content.formattedProgress,
                    isIdeRequired: content.isIdeRequired,
                    itemState: .next,
                    onActivityTap: onActivityTap
                )
            }
        }
    }
}

struct NextLearningActivityView_Previews: PreviewProvider {
    static var previews: some View {
        let nextPlaceholder = StudyPlanWidgetViewStateSectionItem
            .makePlaceholder(state: .next, subtitle: "Hello, coffee!")

        NextLearningActivityView(
            stateKs: .content(
                NextLearningActivityWidgetFeatureViewStateContent(
                    title: nextPlaceholder.title,
                    subtitle: nextPlaceholder.subtitle,
                    isIdeRequired: nextPlaceholder.isIdeRequired,
                    progress: nextPlaceholder.progress,
                    formattedProgress: nextPlaceholder.formattedProgress
                )
            ),
            onActivityTap: {},
            onReloadButtonTap: {}
        )
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
