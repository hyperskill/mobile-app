import shared
import SwiftUI

extension TopicToDiscoverNextButtonView {
    struct Appearance {
        let topicIconWidthHeight: CGFloat = 12

        let learnNextBadgeVerticalPadding: CGFloat = 4
    }
}

struct TopicToDiscoverNextButtonView: View {
    private(set) var appearance = Appearance()

    let topic: Topic
    let isLearnNext: Bool
    let onTap: () -> Void

    var body: some View {
        Button(
            action: onTap,
            label: {
                HStack {
                    Text(topic.title)
                        .font(.body)
                        .foregroundColor(.primaryText)

                    Spacer()

                    if let topicProgress = topic.progress {
                        if topicProgress.isCompleted {
                            Image(systemName: "checkmark")
                                .renderingMode(.template)
                                .resizable()
                                .foregroundColor(Color(ColorPalette.secondary))
                                .frame(widthHeight: appearance.topicIconWidthHeight)
                        } else if topicProgress.isSkipped {
                            Image(Images.Track.TopicsToDiscoverNext.skippedTopic)
                                .renderingMode(.template)
                                .resizable()
                                .scaledToFit()
                                .foregroundColor(Color(ColorPalette.onSurfaceAlpha38))
                                .frame(widthHeight: appearance.topicIconWidthHeight)
                        } else if topicProgress.completenessPercentage > 0 {
                            Text("\(Int(topicProgress.completenessPercentage.rounded()))%")
                                .font(.subheadline)
                                .foregroundColor(Color(ColorPalette.secondary))
                        }
                    }
                }
            }
        )
        .buttonStyle(OutlineButtonStyle(borderColor: .border, alignment: .leading))
        .background(
            completenessBar(
                completenessPercentage: topic.progress?.completenessPercentage ?? 0
            )
        )
        .overlay(learnNextBadge, alignment: .topLeading)
    }

    // MARK: Private API

    @ViewBuilder private func completenessBar(completenessPercentage: Float) -> some View {
        GeometryReader { geometry in
            Rectangle()
                .stroke(lineWidth: 0)
                .background(Color(ColorPalette.overlayGreenAlpha7))
                .cornerRadius(8)
                .frame(width: geometry.size.width * CGFloat(completenessPercentage) / 100)
        }
    }

    @ViewBuilder private var learnNextBadge: some View {
        if isLearnNext {
            GeometryReader { geometry in
                Text("Learn next")
                    .font(.caption)
                    .foregroundColor(Color(ColorPalette.primary))
                    .padding(.horizontal, LayoutInsets.smallInset)
                    .padding(.vertical, appearance.learnNextBadgeVerticalPadding)
                    .background(Color(ColorPalette.surface))
                    .addBorder(color: Color(ColorPalette.primaryAlpha38))
                    .offset(
                        CGSize(
                            width: LayoutInsets.defaultInset,
                            height: -geometry.size.height / 4
                        )
                    )
            }
        }
    }
}

struct TopicToDiscoverNextButtonView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            TopicToDiscoverNextButtonView(
                topic: .init(
                    id: 4,
                    progressId: "",
                    theoryId: nil,
                    title: "Pro data types",
                    progress: TopicProgress(
                        id: "",
                        stagePosition: 0,
                        repeatedCount: 0,
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
                        id: "",
                        stagePosition: 0,
                        repeatedCount: 0,
                        isCompleted: true,
                        isSkipped: false,
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
                        id: "",
                        stagePosition: 0,
                        repeatedCount: 0,
                        isCompleted: false,
                        isSkipped: false,
                        capacity: 0.4
                    )
                ),
                isLearnNext: true,
                onTap: {}
            )
        }
        .padding()
        .background(Color(ColorPalette.surface))
        .previewLayout(.sizeThatFits)
        .preferredColorScheme(.dark)
    }
}
