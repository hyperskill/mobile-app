import SwiftUI

struct TopicsRepetitionsChartBlock: View {
    let topicsToRepeatCount: Int

    let repeatNextTopicText: String?

    let onRepeatNextTopicTap: () -> Void

    let chartData: [(String, Int)]

    let chartDescription: String

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.largeInset) {
            Text(Strings.TopicsRepetitions.Chart.tryToRecallText)
                .font(.subheadline)
                .foregroundColor(.primaryText)

            TopicsRepetitionsCountView(topicsToRepeatCount: topicsToRepeatCount)

            if let repeatNextTopicText {
                Button(repeatNextTopicText) {
                    onRepeatNextTopicTap()
                }
                .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
            }

            TopicsRepetitionsChartView(data: chartData, chartDescription: chartDescription)
        }
        .padding()
        .background(Color(ColorPalette.surface))
    }
}

struct TopicsRepetitionsChartBlock_Previews: PreviewProvider {
    static var previews: some View {
        TopicsRepetitionsChartBlock(
            topicsToRepeatCount: 4,
            repeatNextTopicText: "Repeat Quotes and multi-line s...",
            onRepeatNextTopicTap: {},
            chartData: [("1 time", 39), ("2 times", 20), ("3 times", 0)],
            chartDescription: Strings.TopicsRepetitions.Chart.description
        )
    }
}
