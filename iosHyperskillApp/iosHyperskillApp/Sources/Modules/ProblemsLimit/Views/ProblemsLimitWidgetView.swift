import SwiftUI

extension ProblemsLimitWidgetView {
    struct Appearance {
        let stackSpacing: CGFloat = 8
        let progressWidth: CGFloat = 56
    }
}

struct ProblemsLimitWidgetView: View {
    private(set) var appearance = Appearance()

    let stepsLimitProgress: Float
    let stepsLimitLabel: String
    let updateInLabel: String?

    var body: some View {
        HStack(spacing: appearance.stackSpacing) {
            LinearGradientProgressView(
                progress: stepsLimitProgress,
                gradientStyle: .problemsLimit
            )
            .frame(width: appearance.progressWidth)

            HStack(alignment: .bottom, spacing: appearance.stackSpacing) {
                Text(stepsLimitLabel)
                    .font(.subheadline)
                    .foregroundColor(.primaryText)
                    .animation(.default, value: stepsLimitLabel)

                if let updateInLabel {
                    Text(updateInLabel)
                        .font(.caption)
                        .foregroundColor(.secondaryText)
                        .animation(.default, value: updateInLabel)
                }
            }
        }
    }
}

#if DEBUG
#Preview {
    ProblemsLimitWidgetView(
        stepsLimitProgress: 0.6,
        stepsLimitLabel: "3/5 problems limit",
        updateInLabel: "Update in 2 hours"
    )
}
#endif
