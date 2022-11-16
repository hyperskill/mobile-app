import SwiftUI

extension TopicsRepetitionsChartAxis {
    struct Appearance {
        let dashCount: CGFloat = 5
        let lineWidth: CGFloat = 1
        let lineHeight: CGFloat = 107
        let labelPadding: CGFloat = 4
    }
}

struct TopicsRepetitionsChartAxis: View {
    private(set) var appearance = Appearance()

    let label: String

    var body: some View {
        VStack(alignment: .center, spacing: appearance.labelPadding) {
            DottedLine()
                .stroke(style: StrokeStyle(lineWidth: appearance.lineWidth, dash: [appearance.dashCount]))
                .frame(width: appearance.lineWidth, height: appearance.lineHeight)
                .foregroundColor(Color(ColorPalette.onSurfaceAlpha12))

            Text(label)
                .font(.caption)
                .foregroundColor(.secondaryText)
        }
    }
}

private struct DottedLine: Shape {
    func path(in rect: CGRect) -> Path {
        var path = Path()
        path.move(to: CGPoint(x: rect.width / 2, y: 0))
        path.addLine(to: CGPoint(x: rect.width / 2, y: rect.height))
        return path
    }
}

struct TopicsRepetitionsChartAxis_Previews: PreviewProvider {
    static var previews: some View {
        TopicsRepetitionsChartAxis(label: "0")
    }
}
