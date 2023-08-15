import SwiftUI

extension ProblemsLimitWidgetView {
    struct Appearance {
        let stackSpacing: CGFloat = 8

        let limitCircleWidthHeight: CGFloat = 8
        let limitCirclesSpacing: CGFloat = 4
    }
}

struct ProblemsLimitWidgetView: View {
    private(set) var appearance = Appearance()

    let stepsLimitLeft: Int
    let stepsLimitTotal: Int
    let stepsLimitLabel: String
    let updateInLabel: String?

    var body: some View {
        HStack(spacing: appearance.stackSpacing) {
            HStack(spacing: appearance.limitCirclesSpacing) {
                ForEach(0..<stepsLimitTotal, id: \.self) { index in
                    Circle()
                        .foregroundColor(
                            index < stepsLimitLeft
                            ? Color(ColorPalette.overlayViolet)
                            : Color(ColorPalette.onSurfaceAlpha12)
                        )
                        .frame(widthHeight: appearance.limitCircleWidthHeight)
                }
            }

            HStack(alignment: .bottom, spacing: appearance.stackSpacing) {
                Text(stepsLimitLabel)
                    .font(.subheadline)
                    .foregroundColor(.primaryText)

                if let updateInLabel {
                    Text(updateInLabel)
                        .font(.caption)
                        .foregroundColor(.secondaryText)
                }
            }
        }
    }
}

struct ProblemsLimitWidgetView_Previews: PreviewProvider {
    static var previews: some View {
        ProblemsLimitWidgetView(
            stepsLimitLeft: 3,
            stepsLimitTotal: 5,
            stepsLimitLabel: "3/5 problems limit",
            updateInLabel: "Update in 2 hours"
        )
    }
}
