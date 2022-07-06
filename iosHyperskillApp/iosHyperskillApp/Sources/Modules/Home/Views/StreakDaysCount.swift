import SwiftUI

struct StreakDaysCount: View {
    let count: UInt

    let todayState: StreakState

    var body: some View {
        VStack(spacing: 20) {
            StreakIcon(state: todayState, widthHeight: 32)

            VStack {
                Text(String(count))
                    .font(.title)

                Text(Strings.Streak.daysText)
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
