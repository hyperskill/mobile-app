import shared
import SwiftUI

extension TopicToDiscoverNextCardView {
    struct Appearance {
        let skeletonHeight: CGFloat = 100
    }
}

struct TopicToDiscoverNextCardView: View {
    private(set) var appearance = Appearance()

    let state: TopicsToDiscoverNextFeatureStateKs
    weak var delegate: (TopicToDiscoverNextCardDelegate)?

    var body: some View {
        switch state {
        case .idle, .loading:
            SkeletonRoundedView()
                .frame(height: appearance.skeletonHeight)
        case .empty:
            EmptyView()
        case .error:
            Button(
                Strings.Placeholder.networkErrorButtonText,
                action: {
                    delegate?.doTopicToDiscoverNextCardReloadAction()
                }
            )
            .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
        case .content(let data):
            VStack(alignment: .leading, spacing: LayoutInsets.largeInset) {
                Text(Strings.Home.topicsToDiscoverNextTitle)
                    .font(.subheadline)
                    .foregroundColor(.secondaryText)

                if let topic = data.topicsToDiscoverNext.first {
                    TopicToDiscoverNextButtonView(
                        appearance: .init(buttonStyle: backgroundSurfaceButtonStyle),
                        topic: topic,
                        isLearnNext: true,
                        onTap: {
                            delegate?.doTopicToDiscoverNextCardTapAction(topicID: topic.id)
                        }
                    )
                }
            }
        }
    }

    // MARK: Private API

    private var backgroundSurfaceButtonStyle: OutlineButtonStyle {
        var defaultStyle = TopicToDiscoverNextButtonView.Appearance().buttonStyle

        defaultStyle.backgroundColor = Color(ColorPalette.surface)

        return defaultStyle
    }
}

struct TopicToDiscoverNextCardView_Previews: PreviewProvider {
    static var previews: some View {
        TopicToDiscoverNextCardView(
            state: .init(
                TopicsToDiscoverNextFeatureStateContent(
                    topicsToDiscoverNext: [
                        .init(
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
                                capacity: 0.50
                            )
                        )
                    ],
                    isRefreshing: false
                )
            )
        )
        .padding()
        .preferredColorScheme(.dark)
    }
}
