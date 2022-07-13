import SwiftUI

extension StreakCardView {
    struct Appearance {
        let shadowColor = Color.black.opacity(0.05)
        let shadowRadius: CGFloat = 8
        let shadowX: CGFloat = 0
        let shadowY: CGFloat = 2
    }
}

struct StreakCardView: View {
    private(set) var appearance = Appearance()

    let currentStreak: Int
    let currentStreakCountString: String

    let maxStreak: Int
    let daysStates: [StreakDayState]

    var body: some View {
        StreakView(
            currentStreak: currentStreak,
            currentStreakCountString: currentStreakCountString,
            maxStreak: maxStreak,
            daysStates: daysStates
        )
        .padding()
        .background(Color(ColorPalette.surface))
        .addBorder()
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
        Group {
            StreakCardView(
                currentStreak: 3,
                currentStreakCountString: "3 days",
                maxStreak: 3,
                daysStates: [.passive, .passive, .active, .active, .frozen]
            )

            StreakCardView(
                currentStreak: 0,
                currentStreakCountString: "0 days",
                maxStreak: 3,
                daysStates: [.passive, .passive, .active, .passive, .passive]
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
