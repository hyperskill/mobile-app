import SwiftUI

extension StreakDaysCount {
    struct Appearance {
        let streakIconSize: CGFloat = 32
        let spacing: CGFloat = 20
    }
}

struct StreakDaysCount: View {
    let count: Int

    let todayState: StreakState

    let appearance = Appearance()

    var body: some View {
        VStack(spacing: appearance.spacing) {
            StreakIcon(state: todayState, widthHeight: appearance.streakIconSize)

            VStack {
                Text(String(count))
                    .font(.title)
                    .foregroundColor(.primaryText)

                Text(Strings.Streak.daysText)
                    .font(.subheadline)
                    .foregroundColor(.primaryText)
            }
            .padding()
            .addBorder(color: Color(ColorPalette.onSurfaceAlpha12))
        }
    }
}

struct StreakDaysCount_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StreakDaysCount(count: 0, todayState: .passive)

            StreakDaysCount(count: 5, todayState: .frozen)

            StreakDaysCount(count: 326, todayState: .active)
        }
        .previewLayout(.sizeThatFits)
    }
}
