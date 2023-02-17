import shared
import SwiftUI

extension TopicToDiscoverNextButtonView {
    struct Appearance {
        let rightDetailIconWidthHeight: CGFloat = 12

        let backgroundProgressColor = Color(ColorPalette.overlayGreenAlpha7)

        var buttonStyle = OutlineButtonStyle(borderColor: .border, alignment: .leading, paddingEdgeSet: [])

        let learnNextBadgeVerticalPadding: CGFloat = 4
        let learnNextBadgeVerticalOffsetRatio = -0.5

        let arrowIconSize: CGFloat = 32
    }
}

struct TopicToDiscoverNextButtonView: View {
    private(set) var appearance = Appearance()

    let topic: Topic

    let isLearnNext: Bool

    let onTap: () -> Void

    @State private var badgeViewSize = CGSize.zero

    var body: some View {
        Button(
            action: onTap,
            label: {
                HStack(spacing: LayoutInsets.smallInset) {
                    Text(topic.title)
                        .font(.body)
                        .foregroundColor(.primaryText)

                    Spacer()

                    if let topicProgress = topic.progress {
                        if topicProgress.isCompleted {
                            Image(systemName: "checkmark")
                                .resizable()
                                .renderingMode(.template)
                                .aspectRatio(contentMode: .fit)
                                .foregroundColor(Color(ColorPalette.secondary))
                                .frame(widthHeight: appearance.rightDetailIconWidthHeight)
                        } else if topicProgress.isSkipped {
                            Image(Images.Track.TopicsToDiscoverNext.skippedTopic)
                                .resizable()
                                .renderingMode(.template)
                                .aspectRatio(contentMode: .fit)
                                .foregroundColor(Color(ColorPalette.onSurfaceAlpha38))
                                .frame(widthHeight: appearance.rightDetailIconWidthHeight)
                        } else if topicProgress.completenessPercentage > 0 {
                            Text("\(Int(topicProgress.completenessPercentage.rounded()))%")
                                .font(.subheadline)
                                .foregroundColor(Color(ColorPalette.secondary))
                        }

                        if isLearnNext {
                            Image(Images.Home.ProblemOfDay.arrowUncompleted)
                                .renderingMode(.original)
                                .resizable()
                                .frame(widthHeight: appearance.arrowIconSize)
                        }
                    }
                }
                .padding()
                .frame(minHeight: appearance.buttonStyle.minHeight)
                .background(
                    buildCompletenessBar(
                        completenessPercentage:
                            topic.progress?.completenessPercentage ?? 0
                    )
                )
            }
        )
        .buttonStyle(appearance.buttonStyle)
        // TODO: apply bounce animation to badge
        .overlay(learnNextBadge, alignment: .topLeading)
    }

    // MARK: Private API

    @ViewBuilder
    private func buildCompletenessBar(completenessPercentage: Float) -> some View {
        GeometryReader { geometry in
            Rectangle()
                .stroke(lineWidth: 0)
                .background(appearance.backgroundProgressColor)
                .cornerRadius(appearance.buttonStyle.cornerRadius)
                .frame(
                    width: geometry.size.width * CGFloat(completenessPercentage) / 100
                )
        }
    }

    @ViewBuilder
    private var learnNextBadge: some View {
        if isLearnNext {
            Text(Strings.Track.TopicsToDiscoverNext.learnNextBadge)
                .font(.caption)
                .foregroundColor(Color(ColorPalette.primary))
                .padding(.horizontal, LayoutInsets.smallInset)
                .padding(.vertical, appearance.learnNextBadgeVerticalPadding)
                .background(Color(ColorPalette.surface))
                .addBorder(color: Color(ColorPalette.primaryAlpha38))
                .measureSize { badgeViewSize = $0 }
                .offset(
                    CGSize(
                        width: LayoutInsets.defaultInset,
                        height: badgeViewSize.height * appearance.learnNextBadgeVerticalOffsetRatio
                    )
                )
        }
    }
}

struct TopicToDiscoverNextButtonView_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            TopicToDiscoverNextButtonView(
                topic: .init(
                    id: 4,
                    progressId: "",
                    theoryId: nil,
                    title: "Introduction to operation systems",
                    progress: TopicProgress(
                        isCompleted: false,
                        isSkipped: false,
                        capacity: 0.4
                    )
                ),
                isLearnNext: true,
                onTap: {}
            )

            TopicToDiscoverNextButtonView(
                topic: .init(
                    id: 4,
                    progressId: "",
                    theoryId: nil,
                    title: "Pro data types",
                    progress: TopicProgress(
                        isCompleted: false,
                        isSkipped: true,
                        capacity: 0
                    )
                ),
                isLearnNext: false,
                onTap: {}
            )

            TopicToDiscoverNextButtonView(
                topic: .init(
                    id: 4,
                    progressId: "",
                    theoryId: nil,
                    title: "Pro data types",
                    progress: TopicProgress(
                        isCompleted: true,
                        isSkipped: false,
                        capacity: 0
                    )
                ),
                isLearnNext: false,
                onTap: {}
            )
        }
        .padding()
        .background(Color(ColorPalette.surface))
        .previewLayout(.sizeThatFits)
        .preferredColorScheme(.dark)
    }
}
