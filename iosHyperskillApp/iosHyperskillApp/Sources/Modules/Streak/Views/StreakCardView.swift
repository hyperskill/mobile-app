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

    let isNewStreakRecord: Bool
    let currentStreakCountString: String

    let daysStates: [StreakDayState]

    var body: some View {
        StreakView(
            isNewStreakRecord: isNewStreakRecord,
            currentStreakCountString: currentStreakCountString,
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
                isNewStreakRecord: true,
                currentStreakCountString: "3 days",
                daysStates: [.passive, .passive, .active, .active, .frozen]
            )

            StreakCardView(
                isNewStreakRecord: false,
                currentStreakCountString: "0 days",
                daysStates: [.passive, .passive, .active, .passive, .passive]
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
