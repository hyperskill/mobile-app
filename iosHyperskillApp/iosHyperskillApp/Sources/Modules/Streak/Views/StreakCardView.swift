import SwiftUI

struct StreakCardView: View {
    let isNewStreakRecord: Bool
    let currentStreakCountString: String

    let daysStates: [StreakDayState]

    let streakFreezeState: ProfileFeatureStreakFreezeStateKs?

    var body: some View {
        StreakView(
            isNewStreakRecord: isNewStreakRecord,
            currentStreakCountString: currentStreakCountString,
            daysStates: daysStates,
            streakFreezeState: streakFreezeState
        )
        .padding()
        .background(Color(ColorPalette.surface))
        .addBorder()
    }
}

struct StreakCardView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StreakCardView(
                isNewStreakRecord: true,
                currentStreakCountString: "3 days",
                daysStates: [.passive, .passive, .active, .active, .frozen],
                streakFreezeState: .alreadyHave
            )

            StreakCardView(
                isNewStreakRecord: false,
                currentStreakCountString: "0 days",
                daysStates: [.passive, .passive, .active, .passive, .passive],
                streakFreezeState: .alreadyHave
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
