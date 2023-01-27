import shared
import SwiftUI

extension TrackTopicsToDiscoverNextBlockView {
    struct Appearance {
        let insets = LayoutInsets(horizontal: LayoutInsets.defaultInset, vertical: LayoutInsets.largeInset)

        var spacing = LayoutInsets.defaultInset

        let topicIconWidthHeight: CGFloat = 12
    }
}

struct TrackTopicsToDiscoverNextBlockView: View {
    private(set) var appearance = Appearance()

    var state: TopicsToDiscoverNextFeatureStateKs

    var onTopicTapped: ((Int64) -> Void)?
    var onErrorButtonTapped: (() -> Void)?

    private var shouldHideView: Bool {
        if case .empty = state {
            return true
        }
        return false
    }

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            Text(Strings.Track.TopicsToDiscoverNext.title)
                .font(.title3)
                .foregroundColor(.primaryText)
                .bold()

            switch state {
            case .idle, .loading:
                TrackTopicsToDiscoverNextSkeletonListView()
            case .error:
                Button(
                    Strings.Placeholder.networkErrorButtonText,
                    action: { onErrorButtonTapped?() }
                )
                .buttonStyle(OutlineButtonStyle())
            case .empty:
                EmptyView()
            case .content(let data):
                buildTopicsList(topics: data.topicsToDiscoverNext)
                buildTopicsList(topics: data.topicsToDiscoverNext)
            }
        }
        .frame(maxWidth: .infinity)
        .padding(appearance.insets.edgeInsets)
        .background(BackgroundView(color: Color(ColorPalette.surface)))
        .hidden(shouldHideView)
    }

    @ViewBuilder
    private func buildTopicsList(topics: [Topic]) -> some View {
        LazyVStack(spacing: appearance.spacing) {
            ForEach(topics, id: \.id) { topic in
                Button(
                    action: {
                        onTopicTapped?(topic.id)
                    },
                    label: {
                        HStack {
                            Text(topic.title)
                                .font(.body)
                                .foregroundColor(.primaryText)

                            Spacer()

                            if let topicProgress = topic.progress {
                                if topicProgress.isCompleted {
                                    Image(Images.Track.TopicsToDiscoverNext.completedTopic)
                                        .resizable()
                                        .frame(widthHeight: appearance.topicIconWidthHeight)
                                } else if topicProgress.isSkipped {
                                    Image(Images.Track.TopicsToDiscoverNext.skippedTopic)
                                        .resizable()
                                        .scaledToFit()
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
                    GeometryReader { geometry in
                        Rectangle()
                            .stroke(lineWidth: 0)
                            .background(Color(ColorPalette.overlayGreenAlpha7))
                            .cornerRadius(8)
                            .frame(
                                width: geometry.size.width *
                                   CGFloat(topic.progress?.completenessPercentage ?? 0) / 100
                            )
                    }
                )
            }
        }
    }
}

struct TrackTopicsToDiscoverNextBlockView_Previews: PreviewProvider {
    static var previews: some View {
        TrackTopicsToDiscoverNextBlockView(state: .loading)
        TrackTopicsToDiscoverNextBlockView(state: .error)
        TrackTopicsToDiscoverNextBlockView(state: .empty)
        TrackTopicsToDiscoverNextBlockView(
            state: .content(
                .init(
                    topicsToDiscoverNext: [
                        .init(id: 1, progressId: "", theoryId: nil, title: "Basic data types", progress: nil),
                        .init(id: 2, progressId: "", theoryId: nil, title: "Variables", progress: nil),
                        .init(id: 3, progressId: "", theoryId: nil, title: "Integer arithmetic", progress: nil),
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
                                isSkipped: true,
                                capacity: 0
                            )
                        )
                    ],
                    isRefreshing: false
                )
            )
        )
    }
}
