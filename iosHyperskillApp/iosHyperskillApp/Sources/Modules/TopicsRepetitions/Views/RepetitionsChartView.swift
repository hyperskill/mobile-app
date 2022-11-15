import SwiftUI

struct RepetitionsChartView: View {
    let formatter = Formatter.default

    private let data: [(Int, Int)] = [(1, 32), (2, 16), (3, 0)]

    var body: some View {
        VStack(alignment: .center, spacing: LayoutInsets.smallInset) {
            Text(Strings.TopicsRepetitions.repeatedTopics)
                .font(.title3)
                .foregroundColor(.primaryText)

            let maxCount = data.max(by: { lhs, rhs in lhs.1 < rhs.1 })?.1 ?? 1
            let roundedMaxCount = Double(maxCount + (10 - maxCount % 10))

            VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
                ForEach(data, id: \.0) { (times, count) in
                    RepetitionsChartBar(label: formatter.timesCount(times), value: count, maxValue: roundedMaxCount)
                }
            }
            .frame(maxWidth: .infinity)
            .overlay(
                HStack {
                    RepetitionsChartAxis()
                        .padding(.leading, RepetitionsChartBar.Appearance().labelWidth + LayoutInsets.smallInset + 2)
                    Spacer()
                    RepetitionsChartAxis()
                    Spacer()
                    RepetitionsChartAxis()
                        .padding(.trailing, LayoutInsets.smallInset)
                }
            )

            HStack {
                Text("0")
                    .font(.caption)
                    .foregroundColor(.secondaryText)
                    .padding(.leading, RepetitionsChartBar.Appearance().labelWidth + LayoutInsets.smallInset)

                Spacer()

                Text(String(Int(roundedMaxCount / 2)))
                    .font(.caption)
                    .foregroundColor(.secondaryText)

                Spacer()

                Text(String(Int(roundedMaxCount)))
                    .font(.caption)
                    .foregroundColor(.secondaryText)
            }

            Text(Strings.TopicsRepetitions.youHaveRepeated)
                .font(.subheadline)
                .foregroundColor(.primaryText)
        }
        .padding()
        .addBorder()
    }
}

struct RepetitionsChartView_Previews: PreviewProvider {
    static var previews: some View {
        RepetitionsChartView()
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))

        RepetitionsChartView()
            .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))
            .preferredColorScheme(.dark)

        RepetitionsChartView()
            .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
    }
}
