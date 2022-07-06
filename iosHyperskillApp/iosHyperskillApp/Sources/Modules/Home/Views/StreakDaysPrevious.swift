import SwiftUI

extension StreakDaysPrevious {
    struct Appearance {
        let streakIconSize: CGFloat = 20
    }
}

struct StreakDaysPrevious: View {
    let previousDays: [StreakState]

    let appearance = Appearance()

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
            HStack(spacing: .zero) {
                ForEach(Array(previousDays.enumerated()), id: \.offset) { index, day in
                    StreakIcon(state: day, widthHeight: appearance.streakIconSize)
                    if index != previousDays.count - 1 {
                        Spacer()
                    }
                }
            }

            Text(Strings.Streak.previousFiveDaysText)
                .font(.subheadline)
                .foregroundColor(.secondaryText)
        }
    }
}

struct StreakDaysPrevious_Previews: PreviewProvider {
    static var previews: some View {
        StreakDaysPrevious(previousDays: [.passive, .passive, .frozen, .active, .active])
            .previewLayout(.sizeThatFits)
    }
}
