import SwiftUI

extension RepetitionsChartBar {
    struct Appearance {
        let labelWidth: CGFloat = 44
        let barWidthModifier = 0.8
        let minBarWidth = 20.0
        let barCornerRadius: CGFloat = 4
        let barHeight: CGFloat = 24
        let barLabelTrailingPadding: CGFloat = 6
    }
}

struct RepetitionsChartBar: View {
    private(set) var appearance = Appearance()

    let label: String

    let value: Int

    let maxValue: Double

    var body: some View {
        GeometryReader { geometry in
            let fullBarWidth = (geometry.size.width - appearance.labelWidth) * appearance.barWidthModifier

            let barWidth = max((Double(fullBarWidth) / maxValue) * Double(value), appearance.minBarWidth)

            HStack(alignment: .center, spacing: LayoutInsets.defaultInset) {
                Text(label)
                    .font(.caption)
                    .foregroundColor(.secondaryText)
                    .frame(width: appearance.labelWidth, alignment: .leading)
                RoundedRectangle(cornerRadius: appearance.barCornerRadius)
                    .fill(Color(ColorPalette.secondaryAlpha38))
                    .frame(width: CGFloat(barWidth), alignment: .trailing)
                    .overlay(
                        Text(String(value))
                            .font(.caption2)
                            .foregroundColor(Color(ColorPalette.secondary))
                            .frame(maxWidth: .infinity, alignment: .trailing)
                            .padding(.trailing, appearance.barLabelTrailingPadding)
                    )
                    .addBorder(color: Color(ColorPalette.secondary), cornerRadius: appearance.barCornerRadius)
            }
        }
        .frame(height: appearance.barHeight)
    }
}

struct RepetitionsChartBar_Previews: PreviewProvider {
    static var previews: some View {
        RepetitionsChartBar(label: "1 time", value: 15, maxValue: 20)
    }
}
