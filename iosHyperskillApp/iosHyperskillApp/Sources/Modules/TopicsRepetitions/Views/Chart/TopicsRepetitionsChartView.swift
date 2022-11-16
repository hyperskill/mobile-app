import SwiftUI

extension TopicsRepetitionsChartView {
    struct Appearance {
        let firstAxisPadding = TopicsRepetitionsChartBar.Appearance().labelWidth + LayoutInsets.smallInset
    }
}

struct TopicsRepetitionsChartView: View {
    private(set) var appearance = Appearance()

    let formatter = Formatter.default

    private let data: [(Int, Int)] = [(1, 39), (2, 20), (3, 0)]

    var body: some View {
        VStack(alignment: .center, spacing: LayoutInsets.defaultInset) {
            Text(Strings.TopicsRepetitions.Chart.title)
                .font(.title3)
                .foregroundColor(.primaryText)
                .bold()

            let maxCount = data.max(by: { lhs, rhs in lhs.1 < rhs.1 })?.1 ?? 1
            let roundedMaxCount = Double(maxCount + (10 - maxCount % 10))

            VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
                ForEach(data, id: \.0) { (times, count) in
                    TopicsRepetitionsChartBar(
                        label: formatter.timesCount(times),
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

            Text(Strings.TopicsRepetitions.Chart.description)
                .font(.subheadline)
                .foregroundColor(.primaryText)
                .padding(.top, LayoutInsets.smallInset)
        }
        .padding()
        .addBorder()
    }
}

struct TopicsRepetitionsChartView_Previews: PreviewProvider {
    static var previews: some View {
        TopicsRepetitionsChartView()
            .previewDevice(PreviewDevice(rawValue: "iPhone 13 Pro"))

        TopicsRepetitionsChartView()
            .previewDevice(PreviewDevice(rawValue: "iPhone SE (3rd generation)"))
            .preferredColorScheme(.dark)

        TopicsRepetitionsChartView()
            .previewDevice(PreviewDevice(rawValue: "iPad (9th generation)"))
    }
}
