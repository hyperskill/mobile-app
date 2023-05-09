import shared
import SwiftUI

extension TopicToDiscoverNextCardView {
    struct Appearance {
        let topicButtonStyle: OutlineButtonStyle = {
            var defaultStyle = TopicToDiscoverNextButtonView.Appearance().buttonStyle

            defaultStyle.backgroundColor = Color(ColorPalette.surface)

            return defaultStyle
        }()
    }
}

struct TopicToDiscoverNextCardView: View {
    private(set) var appearance = Appearance()

    let state: TopicsToDiscoverNextFeatureStateKs

    weak var delegate: TopicToDiscoverNextCardDelegate?

    var body: some View {
        switch state {
        case .idle, .loading:
            TopicToDiscoverNextCardSkeletonView()
        case .empty:
            EmptyView()
        case .error:
            buildContainer {
                Button(
                    Strings.Placeholder.networkErrorButtonText,
                    action: {
                        delegate?.doTopicToDiscoverNextCardReloadAction()
                    }
                )
                .buttonStyle(OutlineButtonStyle())
            }
        case .content(let data):
            buildContainer {
                if let topic = data.topicsToDiscoverNext.first {
                    TopicToDiscoverNextButtonView(
                        appearance: .init(buttonStyle: appearance.topicButtonStyle),
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

    @ViewBuilder
    private func buildContainer(@ViewBuilder content: () -> some View) -> some View {
        VStack(alignment: .leading, spacing: LayoutInsets.largeInset) {
            Text(Strings.Home.topicsToDiscoverNextTitle)
                .font(.subheadline)
                .foregroundColor(.secondaryText)

            content()
        }
    }
}

struct TopicToDiscoverNextCardView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: LayoutInsets.largeInset) {
            TopicToDiscoverNextCardView(state: .loading)

            TopicToDiscoverNextCardView(state: .error)

            TopicToDiscoverNextCardView(state: .empty)

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
                                    isCompleted: false,
                                    isSkipped: false,
                                    capacity: 0.5
                                )
                            )
                        ],
                        isRefreshing: false
                    )
                )
            )
            .preferredColorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
