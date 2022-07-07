import SwiftUI

extension StreakCardView {
    struct Appearance {
        let streakIconSize: CGFloat = 20
        let todayBorderWidth: CGFloat = 2
        let todayBorderRadius: CGFloat = 4
        let cardBorderWidth: CGFloat = 1
        let cardBorderRadius: CGFloat = 8
        let cardShadowOpacity: Double = 0.05
        let cardShadowRadius: CGFloat = 8
        let cardShadowY: CGFloat = 2
    }
}

struct StreakCardView: View {
    let count: Int
    let todayState: StreakState
    let previousDays: [StreakState]

    let appearance = Appearance()


    var body: some View {
        HStack(spacing: LayoutInsets.defaultInset) {
            StreakDaysCount(count: count, todayState: todayState)

            VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
                VStack(alignment: .leading, spacing: .zero) {
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
        .addBorder(
            color: Color(ColorPalette.onSurfaceAlpha12),
            width: appearance.cardBorderWidth,
            cornerRadius: appearance.cardBorderRadius
        )
        .background(
            Color(ColorPalette.surface)
            .shadow(
                color: .black.opacity(appearance.cardShadowOpacity),
                radius: appearance.cardShadowRadius,
                x: .zero,
                y: appearance.cardShadowY
            )
        )
    }
}

struct StreakCardView_Previews: PreviewProvider {
    static var previews: some View {
        StreakCardView(count: 3, todayState: .active, previousDays: [.active, .passive, .passive, .frozen, .active])
    }
}
