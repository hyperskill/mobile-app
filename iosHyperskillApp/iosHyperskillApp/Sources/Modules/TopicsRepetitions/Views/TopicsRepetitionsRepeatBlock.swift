import shared
import SwiftUI

extension TopicsRepetitionsRepeatBlock {
    struct Appearance {
        let buttonStyle = OutlineButtonStyle(borderColor: .border, alignment: .leading)
    }
}

struct TopicsRepetitionsRepeatBlock: View {
    private(set) var appearance = Appearance()

    let repeatBlockTitle: String

    let trackTopicsTitle: String

    let topicsToRepeatFromCurrentTrack: [TopicToRepeat]

    let topicsToRepeatFromOtherTracks: [TopicToRepeat]

    let onTopicButtonTapped: (Int64) -> Void

    let showMoreButtonState: ShowMoreButtonState

    let onShowMoreButtonTap: () -> Void

    let topicsToRepeatWillLoadedCount: Int

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.largeInset) {
            Text(repeatBlockTitle)
                .font(.title3)
                .foregroundColor(.primaryText)
                .bold()
                .frame(maxWidth: .infinity, alignment: .leading)

            if !topicsToRepeatFromCurrentTrack.isEmpty {
                Text(trackTopicsTitle)
                    .font(.subheadline)
                    .foregroundColor(.secondaryText)

                buildRepeatList(
                    topicsToRepeat: topicsToRepeatFromCurrentTrack,
                    isShowingSkeletons: topicsToRepeatFromOtherTracks.isEmpty
                        && showMoreButtonState == ShowMoreButtonState.loading
                )
            }

            if !topicsToRepeatFromOtherTracks.isEmpty {
                Text(Strings.TopicsRepetitions.RepeatBlock.otherTracks)
                    .font(.subheadline)
                    .foregroundColor(.secondaryText)

                buildRepeatList(
                    topicsToRepeat: topicsToRepeatFromOtherTracks,
                    isShowingSkeletons: showMoreButtonState == ShowMoreButtonState.loading
                )
            }

            if case ShowMoreButtonState.available = showMoreButtonState {
                ShowMoreButton {
                    onShowMoreButtonTap()
                }
            }
        }
        .padding()
        .background(Color(ColorPalette.surface))
    }

    private func buildRepeatList(topicsToRepeat: [TopicToRepeat], isShowingSkeletons: Bool) -> some View {
        LazyVStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
            ForEach(topicsToRepeat, id: \.topicId) { topicToRepeat in
                Button(
                    action: {
                        onTopicButtonTapped(topicToRepeat.topicId)
                    },
                    label: {
                        Text(topicToRepeat.title)
                            .font(.body)
                            .foregroundColor(.primaryText)
                    }
                )
                .buttonStyle(appearance.buttonStyle)
            }

            if isShowingSkeletons {
                ForEach(0..<topicsToRepeatWillLoadedCount, id: \.self) { _ in
                    SkeletonRoundedView()
                        .frame(height: appearance.buttonStyle.minHeight)
                }
            }
        }
    }
}

struct TopicsRepetitionsRepeatBlock_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            TopicsRepetitionsRepeatBlock(
                repeatBlockTitle: "All 8 topics to repeat",
                trackTopicsTitle: "Topics from track Python Core",
                topicsToRepeatFromCurrentTrack: [
                    TopicToRepeat(topicId: 1, title: "Variables"),
                    TopicToRepeat(topicId: 2, title: "Quotes and multi-line strings"),
                    TopicToRepeat(topicId: 3, title: "Basic data types")
                ],
                topicsToRepeatFromOtherTracks: [],
                onTopicButtonTapped: { _ in },
                showMoreButtonState: ShowMoreButtonState.loading,
                onShowMoreButtonTap: {},
                topicsToRepeatWillLoadedCount: 5
            )

            TopicsRepetitionsRepeatBlock(
                repeatBlockTitle: "All topics to repeat",
                trackTopicsTitle: "Topics from track Python Core",
                topicsToRepeatFromCurrentTrack: [],
                topicsToRepeatFromOtherTracks: [
                    TopicToRepeat(topicId: 1, title: "Variables"),
                    TopicToRepeat(topicId: 2, title: "Quotes and multi-line strings"),
                    TopicToRepeat(topicId: 3, title: "Basic data types")
                ],
                onTopicButtonTapped: { _ in },
                showMoreButtonState: ShowMoreButtonState.empty,
                onShowMoreButtonTap: {},
                topicsToRepeatWillLoadedCount: 5
            )

            TopicsRepetitionsRepeatBlock(
                repeatBlockTitle: "All topics to repeat",
                trackTopicsTitle: "Topics from track Python Core",
                topicsToRepeatFromCurrentTrack: [
                    TopicToRepeat(topicId: 1, title: "Variables"),
                    TopicToRepeat(topicId: 2, title: "Quotes and multi-line strings"),
                    TopicToRepeat(topicId: 3, title: "Basic data types")
                ],
                topicsToRepeatFromOtherTracks: [
                    TopicToRepeat(topicId: 1, title: "Variables"),
                    TopicToRepeat(topicId: 2, title: "Quotes and multi-line strings"),
                    TopicToRepeat(topicId: 3, title: "Basic data types")
                ],
                onTopicButtonTapped: { _ in },
                showMoreButtonState: ShowMoreButtonState.available,
                onShowMoreButtonTap: {},
                topicsToRepeatWillLoadedCount: 5
            )
        }
        .previewLayout(.sizeThatFits)
    }
}
