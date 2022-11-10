import SwiftUI

extension StepTheoryContentView {
    struct Appearance {
        let interItemSpacing: CGFloat = 24

        let stepTextFont = UIFont.preferredFont(forTextStyle: .body)
        let stepTextColor = UIColor.primaryText
    }
}

struct StepTheoryContentView: View {
    private(set) var appearance = Appearance()

    @State var viewData: StepViewData

    var body: some View {
        ScrollView {
            LazyVStack(alignment: .leading, spacing: appearance.interItemSpacing) {
                StepHeaderView(
                    title: viewData.formattedType,
                    timeToComplete: viewData.formattedTimeToComplete
                )

                // ALTAPPS-397: Hidden
//                StepActionButton(
//                    title: Strings.Step.startPracticing,
//                    style: .greenOutline
//                ) {
//                    print("Start practicing tapped")
//                }

                LatexView(
                    text: $viewData.text,
                    configuration: .init(
                        appearance: .init(labelFont: appearance.stepTextFont),
                        contentProcessor: ContentProcessor(
                            injections: ContentProcessor.defaultInjections + [
                                StepStylesInjection(),
                                FontInjection(font: appearance.stepTextFont),
                                TextColorInjection(dynamicColor: appearance.stepTextColor)
                            ]
                        )
                    )
                )

                // ALTAPPS-397: Hidden
//                StepBottomControlsView(
//                    commentStatisticsViewData: viewData.commentsStatistics,
//                    onStartPracticingClick: {
//                        print("Start practicing tapped")
//                    },
//                    onCommentStatisticClick: { commentStatistic in
//                        print("Comment statistic clicked = \(commentStatistic)")
//                    }
//                )
            }
            .padding()
        }
    }
}

#if DEBUG
struct StepContentView_Previews: PreviewProvider {
    static var previews: some View {
        StepTheoryContentView(viewData: .placeholder)
    }
}
#endif
