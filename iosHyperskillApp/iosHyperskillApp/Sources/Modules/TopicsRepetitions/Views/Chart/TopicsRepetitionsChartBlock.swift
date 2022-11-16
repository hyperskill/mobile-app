import SwiftUI

struct TopicsRepetitionsChartBlock: View {
    let topicsToRepeatCount: Int

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.largeInset) {
            Text(Strings.TopicsRepetitions.Chart.tryToRecallText)
                .font(.subheadline)
                .foregroundColor(.primaryText)

            TopicsRepetitionsCountView(topicsToRepeatCount: topicsToRepeatCount)

            Button("Repeat Quotes and multi-line s...") {
            }
            .buttonStyle(RoundedRectangleButtonStyle(style: .violet))

            TopicsRepetitionsChartView()
        }
        .padding()
        .background(Color(ColorPalette.surface))
    }
}

struct TopicsRepetitionsChartBlock_Previews: PreviewProvider {
    static var previews: some View {
        TopicsRepetitionsChartBlock(topicsToRepeatCount: 4)
    }
}
