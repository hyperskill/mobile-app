import SwiftUI

extension StreakCardView {
    struct Appearance {
        let streakIconSize: CGFloat = 20

        let todayBorderWidth: CGFloat = 2
        let todayBorderRadius: CGFloat = 4

        let shadowColor = Color.black.opacity(0.05)
        let shadowRadius: CGFloat = 8
        let shadowX: CGFloat = 0
        let shadowY: CGFloat = 2
    }
}

struct StreakCardView: View {
    private(set) var appearance = Appearance()

    let count: Int
    let todayState: StreakState
    let previousDays: [StreakState]

    var body: some View {
        HStack(spacing: LayoutInsets.defaultInset) {
            StreakDaysCount(count: count, todayState: todayState)

            VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
                VStack(alignment: .leading, spacing: 0) {
                    Text(Strings.Streak.solvingProblemText)
                        .font(.title3)
                        .foregroundColor(.primaryText)

                    Text(Strings.Streak.keepSolvingProblemsText)
                        .font(.subheadline)
                        .foregroundColor(.secondaryText)
                }

                HStack(spacing: LayoutInsets.smallInset) {
                    StreakDaysPrevious(previousDays: previousDays)

                    VStack(spacing: LayoutInsets.smallInset) {
                        StreakIcon(state: todayState, widthHeight: appearance.streakIconSize)

                        Text(Strings.Streak.todayText)
                            .font(.subheadline)
                            .foregroundColor(.secondaryText)
                    }
                    .padding()
                    .addBorder(
                        color: Color(ColorPalette.primary),
                        width: appearance.todayBorderWidth,
                        cornerRadius: appearance.todayBorderRadius
                    )
                }
            }
        }
        .padding()
        .background(Color(ColorPalette.surface))
        .addBorder(color: Color(ColorPalette.onSurfaceAlpha12))
        .shadow(
            color: appearance.shadowColor,
            radius: appearance.shadowRadius,
            x: appearance.shadowX,
            y: appearance.shadowY
        )
    }
}

struct StreakCardView_Previews: PreviewProvider {
    static var previews: some View {
        StreakCardView(
            count: 3,
            todayState: .active,
            previousDays: [.active, .passive, .passive, .frozen, .active]
        )
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
