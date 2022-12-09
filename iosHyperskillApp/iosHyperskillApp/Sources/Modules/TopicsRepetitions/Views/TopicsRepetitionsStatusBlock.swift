import shared
import SwiftUI

struct TopicsRepetitionsStatusBlock: View {
    let repetitionsStatus: RepetitionsStatus

    let onRepeatNextTopicTap: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.largeInset) {
            switch RepetitionsStatusKs(repetitionsStatus) {
            case .recommendedTopicsAvailable(let availableStatus):
                Text(Strings.TopicsRepetitions.StatusBlock.tryToRecallText)
                    .font(.subheadline)
                    .foregroundColor(.primaryText)

                TopicsRepetitionsCountView(topicsToRepeatCount: Int(availableStatus.recommendedRepetitionsCount))

                if let repeatButtonText = availableStatus.repeatButtonText {
                    Button(repeatButtonText) {
                        onRepeatNextTopicTap()
                    }
                    .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
                }
            case .recommendedTopicsRepeated:
                Text(Strings.TopicsRepetitions.StatusBlock.goodJobText)
                    .font(.headline)
                    .foregroundColor(.primaryText)
            case .allTopicsRepeated:
                Text(Strings.TopicsRepetitions.StatusBlock.allTopicsRepeated)
                    .font(.subheadline)
                    .foregroundColor(.primaryText)
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
        .background(Color(ColorPalette.surface))
    }
}

struct TopicsRepetitionsStatusBlock_Previews: PreviewProvider {
    static var previews: some View {
        TopicsRepetitionsStatusBlock(
            repetitionsStatus: RepetitionsStatusRecommendedTopicsAvailable(
                recommendedRepetitionsCount: 5,
                repeatButtonText: "Repeat Strings"
            ),
            onRepeatNextTopicTap: {}
        )

        TopicsRepetitionsStatusBlock(
            repetitionsStatus: RepetitionsStatusRecommendedTopicsRepeated(),
            onRepeatNextTopicTap: {}
        )

        TopicsRepetitionsStatusBlock(
            repetitionsStatus: RepetitionsStatusAllTopicsRepeated(),
            onRepeatNextTopicTap: {}
        )
    }
}
