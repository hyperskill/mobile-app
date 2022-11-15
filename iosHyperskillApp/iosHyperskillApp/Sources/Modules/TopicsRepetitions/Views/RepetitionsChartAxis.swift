import SwiftUI

extension RepetitionsChartAxis {
    struct Appearance {
        let dashCount: CGFloat = 5
        let lineWidth: CGFloat = 1
        let lineHeight: CGFloat = 107
    }
}

struct RepetitionsChartAxis: View {
    private(set) var appearance = Appearance()

    var body: some View {
        DottedLine()
            .stroke(style: StrokeStyle(lineWidth: appearance.lineWidth, dash: [appearance.dashCount]))
            .frame(width: appearance.lineWidth, height: appearance.lineHeight)
            .foregroundColor(Color(ColorPalette.onSurfaceAlpha12))
    }
}

private struct DottedLine: Shape {
    func path(in rect: CGRect) -> Path {
        var path = Path()
        path.move(to: CGPoint(x: 0, y: 0))
        path.addLine(to: CGPoint(x: rect.width, y: rect.height))
        return path
    }
}

struct RepetitionsChartAxis_Previews: PreviewProvider {
    static var previews: some View {
        RepetitionsChartAxis()
    }
}
