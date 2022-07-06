import SwiftUI

struct StreakDaysPrevious: View {
    var days: [StreakState]

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                ForEach(Array(days.enumerated()), id: \.offset) { index, day in
                    StreakIcon(state: day, widthHeight: 20)
                    if index != days.count - 1 {
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
        StreakDaysPrevious(days: [.passive, .passive, .frozen, .active, .active])
            .previewLayout(.sizeThatFits)
    }
}
