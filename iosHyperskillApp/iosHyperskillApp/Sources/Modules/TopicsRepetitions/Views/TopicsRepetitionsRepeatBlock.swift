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

    let repeatButtonsCurrentTrack: [RepeatButtonInfo]

    let repeatButtonsOtherTracks: [RepeatButtonInfo]

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

            if !repeatButtonsCurrentTrack.isEmpty {
                Text(trackTopicsTitle)
                    .font(.subheadline)
                    .foregroundColor(.secondaryText)

                buildRepeatList(repeatButtons: repeatButtonsCurrentTrack)
            }

            if !repeatButtonsOtherTracks.isEmpty {
                Text(Strings.TopicsRepetitions.RepeatBlock.otherTracks)
                    .font(.subheadline)
                    .foregroundColor(.secondaryText)

                buildRepeatList(repeatButtons: repeatButtonsOtherTracks)
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

    private func buildRepeatList(repeatButtons: [RepeatButtonInfo]) -> some View {
        VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
            ForEach(repeatButtons, id: \.topicID) { buttonInfo in
                Button(
                    action: {
                        buttonInfo.onTap()
                    },
                    label: {
                        Text(buttonInfo.title)
                            .font(.body)
                            .foregroundColor(.primaryText)
                    }
                )
                .buttonStyle(appearance.buttonStyle)
            }

            if case ShowMoreButtonState.loading = showMoreButtonState {
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
                repeatButtonsCurrentTrack: [
                    RepeatButtonInfo(topicID: 1, title: "Variables", onTap: {}),
                    RepeatButtonInfo(topicID: 2, title: "Quotes and multi-line strings", onTap: {}),
                    RepeatButtonInfo(topicID: 3, title: "Basic data types", onTap: {})
                ],
                repeatButtonsOtherTracks: [],
                showMoreButtonState: ShowMoreButtonState.loading,
                onShowMoreButtonTap: {},
                topicsToRepeatWillLoadedCount: 5
            )

            TopicsRepetitionsRepeatBlock(
                repeatBlockTitle: "All topics to repeat",
                trackTopicsTitle: "Topics from track Python Core",
                repeatButtonsCurrentTrack: [],
                repeatButtonsOtherTracks: [
                    RepeatButtonInfo(topicID: 1, title: "Variables", onTap: {}),
                    RepeatButtonInfo(topicID: 2, title: "Quotes and multi-line strings", onTap: {}),
                    RepeatButtonInfo(topicID: 3, title: "Basic data types", onTap: {})
                ],
                showMoreButtonState: ShowMoreButtonState.empty,
                onShowMoreButtonTap: {},
                topicsToRepeatWillLoadedCount: 5
            )

            TopicsRepetitionsRepeatBlock(
                repeatBlockTitle: "All topics to repeat",
                trackTopicsTitle: "Topics from track Python Core",
                repeatButtonsCurrentTrack: [
                    RepeatButtonInfo(topicID: 1, title: "Variables", onTap: {}),
                    RepeatButtonInfo(topicID: 2, title: "Quotes and multi-line strings", onTap: {}),
                    RepeatButtonInfo(topicID: 3, title: "Basic data types", onTap: {})
                ],
                repeatButtonsOtherTracks: [
                    RepeatButtonInfo(topicID: 1, title: "Variables", onTap: {}),
                    RepeatButtonInfo(topicID: 2, title: "Quotes and multi-line strings", onTap: {}),
                    RepeatButtonInfo(topicID: 3, title: "Basic data types", onTap: {})
                ],
                showMoreButtonState: ShowMoreButtonState.available,
                onShowMoreButtonTap: {},
                topicsToRepeatWillLoadedCount: 5
            )
        }
        .previewLayout(.sizeThatFits)
    }
}
