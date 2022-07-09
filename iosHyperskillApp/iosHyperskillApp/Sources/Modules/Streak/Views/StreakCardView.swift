import SwiftUI

extension StreakCardView {
    struct Appearance {
        let spacing: CGFloat = 16

        let streakIconSizeDefault: CGFloat = 20
        let streakIconSizeLarge: CGFloat = 36

        let crownLeftMargin: CGFloat = 5
        let crownWidth: CGFloat = 12
        let crownHeight: CGFloat = 10

        let defaultBorderWidth: CGFloat = 1
        let todayBorderWidth: CGFloat = 2

        let shadowColor = Color.black.opacity(0.05)
        let shadowRadius: CGFloat = 8
        let shadowX: CGFloat = 0
        let shadowY: CGFloat = 2
    }
}

struct StreakCardView: View {
    private(set) var appearance = Appearance()

    let currentStreak: Int
    let maxStreak: Int
    let daysStates: [StreakDayState]

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            HStack(spacing: LayoutInsets.defaultInset) {
                StreakIcon(state: daysStates[daysStates.count - 1], widthHeight: appearance.streakIconSizeLarge)


                HStack(alignment: .top, spacing: appearance.crownLeftMargin) {
                    Text("\(currentStreak) \(Strings.Streak.daysText)")
                        .font(.title)
                        .foregroundColor(.primaryText)

                    if currentStreak == maxStreak {
                        Image(Images.Home.Streak.crown)
                            .renderingMode(.original)
                            .resizable()
                            .frame(width: appearance.crownWidth, height: appearance.crownHeight)
                    }
                }

                Spacer()

                Text(Strings.Streak.solvingProblemText)
                    .font(.subheadline)
                    .foregroundColor(.secondaryText)
            }


            HStack(spacing: 0) {
                ForEach(Array(daysStates.enumerated()), id: \.offset) { index, dayState in
                    StreakIcon(state: dayState, widthHeight: appearance.streakIconSizeDefault)
                        .padding(LayoutInsets.smallInset)
                        .addBorder(
                            color: Color(
                                index != daysStates.count - 1
                                ? ColorPalette.onSurfaceAlpha12
                                : ColorPalette.primary
                            ),
                            width: index != daysStates.count - 1
                                   ? appearance.defaultBorderWidth
                                   : appearance.todayBorderWidth
                        )
                    if index != daysStates.count - 1 {
                        Spacer()
                    }
                }
            }

            HStack {
                Text(Strings.Streak.previousFiveDaysText)
                    .font(.subheadline)
                    .foregroundColor(.secondaryText)

                Spacer()

                Text(Strings.Streak.todayText)
                    .font(.subheadline)
                    .foregroundColor(.secondaryText)
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
        Group {
            StreakCardView(
                currentStreak: 3,
                maxStreak: 3,
                daysStates: [.passive, .passive, .active, .active, .frozen]
            )

            StreakCardView(
                currentStreak: 0,
                maxStreak: 3,
                daysStates: [.passive, .passive, .active, .passive, .passive]
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
