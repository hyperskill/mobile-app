import SwiftUI

struct TopicsRepetitionsStatisticView: View {
    let topicsToRepeatCount: Int

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.largeInset) {
            Text(Strings.TopicsRepetitions.tryToRecallText)
                .font(.subheadline)
                .foregroundColor(.secondaryText)

            TopicsRepetitionsCountView(topicsToRepeatCount: topicsToRepeatCount)

            Button("Repeat Quotes and multi-line s...") {
            }
            .buttonStyle(RoundedRectangleButtonStyle(style: .violet))

            RepetitionsChartView()
        }
        .padding()
        .background(Color.white)
    }
}

struct TopicsRepetitionsStatisticView_Previews: PreviewProvider {
    static var previews: some View {
        TopicsRepetitionsStatisticView(topicsToRepeatCount: 4)
    }
}
