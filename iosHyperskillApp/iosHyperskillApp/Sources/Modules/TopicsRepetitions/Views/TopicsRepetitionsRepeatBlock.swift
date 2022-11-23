import shared
import SwiftUI

struct TopicsRepetitionsRepeatBlock: View {
    let repeatBlockTitle: String

    let trackTopicsTitle: String

    let topicsToRepeat: [(Int, String, () -> Void)]

    let showMoreButtonState: ShowMoreButtonState

    let onShowMoreButtonTap: () -> Void

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
                ForEach(topicsToRepeat, id: \.0) { (_, topic, onTap) in
                    Button(
                        action: {
                            onTap()
                        },
                        label: {
                            Text(topic)
                                .font(.body)
                                .foregroundColor(.primaryText)
                        }
                    )
                    .buttonStyle(OutlineButtonStyle(borderColor: .border, alignment: .leading))
                }
            }

            switch showMoreButtonState {
            case ShowMoreButtonState.available:
                ShowMoreButton {
                    onShowMoreButtonTap()
                }
            case ShowMoreButtonState.loading:
                ProgressView()
            case ShowMoreButtonState.empty:
                EmptyView()
            default:
                Text("Unkwown state")
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
            topicsToRepeat: [
                (1, "Variables", {}),
                (2, "Quotes and multi-line strings", {}),
                (3, "Basic data types", {})
            ],
            showMoreButtonState: ShowMoreButtonState.available,
            onShowMoreButtonTap: {}
        )
        .previewLayout(.sizeThatFits)
    }
}
