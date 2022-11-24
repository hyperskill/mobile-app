import shared
import SwiftUI

struct TopicsRepetitionsRepeatBlock: View {
    let repeatBlockTitle: String

    let trackTopicsTitle: String

    let repeatButtons: [RepeatButtonInfo]

    let showMoreButtonState: ShowMoreButtonState

    let onShowMoreButtonTap: () -> Void

    let topicsToRepeatWillLoadedCount: Int

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.largeInset) {
            Text(repeatBlockTitle)
                .font(.title3)
                .foregroundColor(.primaryText)
                .bold()

            Text(trackTopicsTitle)
                .font(.subheadline)
                .foregroundColor(.secondaryText)

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
                    .buttonStyle(OutlineButtonStyle(borderColor: .border, alignment: .leading))
                }

                switch showMoreButtonState {
                case ShowMoreButtonState.available:
                    ShowMoreButton {
                        onShowMoreButtonTap()
                    }
                    .padding(.top, LayoutInsets.largeInset - LayoutInsets.smallInset)
                case ShowMoreButtonState.loading:
                    ForEach(0..<topicsToRepeatWillLoadedCount, id: \.self) { _ in
                        SkeletonRoundedView()
                            .frame(height: OutlineButtonStyle().minHeight)
                    }
                case ShowMoreButtonState.empty:
                    EmptyView()
                default:
                    Text("Unkwown state")
                }
            }
        }
        .padding()
        .background(Color(ColorPalette.surface))
    }
}

struct TopicsRepetitionsRepeatBlock_Previews: PreviewProvider {
    static var previews: some View {
        TopicsRepetitionsRepeatBlock(
            repeatBlockTitle: "All 8 topics to repeat",
            trackTopicsTitle: "Topics from track Python Core",
            repeatButtons: [
                RepeatButtonInfo(topicID: 1, title: "Variables", onTap: {}),
                RepeatButtonInfo(topicID: 2, title: "Quotes and multi-line strings", onTap: {}),
                RepeatButtonInfo(topicID: 3, title: "Basic data types", onTap: {})
            ],
            showMoreButtonState: ShowMoreButtonState.available,
            onShowMoreButtonTap: {},
            topicsToRepeatWillLoadedCount: 5
        )
        .previewLayout(.sizeThatFits)
    }
}
