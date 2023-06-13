import SwiftUI

struct StreakIcon: View {
    let state: StreakDayState

    let widthHeight: CGFloat

    var body: some View {
        Image(state.icon)
            .renderingMode(.original)
            .resizable()
            .frame(widthHeight: widthHeight)
    }
}

fileprivate extension StreakDayState {
    var icon: String {
        switch self {
        case .active:
            return Images.Profile.Streak.Card.streakActive
        case .passive:
            return Images.Profile.Streak.Card.streakPassive
        case .frozen:
            return Images.Profile.Streak.Card.streakFrozen
        case .recovered:
            return Images.Profile.Streak.Card.streakRecovered
        }
    }
}

struct StreakIcon_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StreakIcon(state: .active, widthHeight: 20)

            StreakIcon(state: .recovered, widthHeight: 20)

            StreakIcon(state: .passive, widthHeight: 20)

            StreakIcon(state: .frozen, widthHeight: 20)
        }
        .previewLayout(.sizeThatFits)
    }
}
