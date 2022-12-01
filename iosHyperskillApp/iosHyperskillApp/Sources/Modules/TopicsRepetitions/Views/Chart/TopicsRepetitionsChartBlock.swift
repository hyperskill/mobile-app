import SwiftUI

extension TopicsRepetitionsChartBlock {
    struct Appearance {
        let firstAxisPadding = TopicsRepetitionsChartBar.Appearance().labelWidth + LayoutInsets.smallInset
    }
}

struct TopicsRepetitionsChartBlock: View {
    private(set) var appearance = Appearance()

    let chartData: [(String, Int)]

    let chartDescription: String

    var body: some View {
        VStack(alignment: .center, spacing: LayoutInsets.defaultInset) {
            Text(Strings.TopicsRepetitions.Chart.title)
                .font(.title3)
                .foregroundColor(.primaryText)
                .bold()

            let maxCount = chartData.max(by: { lhs, rhs in lhs.1 < rhs.1 })?.1 ?? 1
            let roundedMaxCount = Double(maxCount + (10 - maxCount % 10))

            VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
                ForEach(chartData, id: \.0) { (label, count) in
                    TopicsRepetitionsChartBar(
                        label: label,
                        value: count,
                        maxValue: roundedMaxCount
                    )
                }
            }
            .frame(maxWidth: .infinity)
            .overlay(
                HStack {
                    TopicsRepetitionsChartAxis(label: "0")
                        .padding(.leading, appearance.firstAxisPadding)
                    Spacer()
                    TopicsRepetitionsChartAxis(label: String(Int(roundedMaxCount / 2)))
                    Spacer()
                    TopicsRepetitionsChartAxis(label: String(Int(roundedMaxCount)))
                        .padding(.trailing, LayoutInsets.smallInset)
                }
                    .padding(.top)
            )

            Text(chartDescription)
                .font(.subheadline)
                .foregroundColor(.primaryText)
                .padding(.top, LayoutInsets.defaultInset)
        }
        .padding()
        .background(Color(ColorPalette.surface))
    }
}

struct TopicsRepetitionsChartBlock_Previews: PreviewProvider {
    static var previews: some View {
        TopicsRepetitionsChartBlock(
            chartData: [("1 time", 39), ("2 times", 20), ("3 times", 0)],
            chartDescription: Strings.TopicsRepetitions.Chart.description
        )
        .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))

        TopicsRepetitionsChartBlock(
            chartData: [("1 time", 39), ("2 times", 20), ("3 times", 0)],
            chartDescription: Strings.TopicsRepetitions.Chart.description
        )
        .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))
        .preferredColorScheme(.dark)

        TopicsRepetitionsChartBlock(
            chartData: [("1 time", 39), ("2 times", 20), ("3 times", 0)],
            chartDescription: Strings.TopicsRepetitions.Chart.description
        )
        .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
    }
}
