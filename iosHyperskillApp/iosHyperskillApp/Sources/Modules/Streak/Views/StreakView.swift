import SwiftUI

extension StreakView {
    struct Appearance {
        let streakIconSizeDefault: CGFloat = 20
        let streakIconSizeLarge: CGFloat = 36

        let crownLeftMargin: CGFloat = 5
        let crownWidth: CGFloat = 12
        let crownHeight: CGFloat = 10

        let defaultBorderWidth: CGFloat = 1
        let todayBorderWidth: CGFloat = 2
    }
}

struct StreakView: View {
    private(set) var appearance = Appearance()

    let isNewStreakRecord: Bool
    let currentStreakCountString: String

    let daysStates: [StreakDayState]

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            HStack(spacing: LayoutInsets.defaultInset) {
                StreakIcon(
                    state: daysStates[daysStates.count - 1],
                    widthHeight: appearance.streakIconSizeLarge
                )

                HStack(alignment: .top, spacing: appearance.crownLeftMargin) {
                    Text(currentStreakCountString)
                        .font(.title)
                        .foregroundColor(.primaryText)

                    if isNewStreakRecord {
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
        .background(Color(ColorPalette.surface))
    }
}

struct StreakView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StreakView(
                isNewStreakRecord: true,
                currentStreakCountString: "3 days",
                daysStates: [.passive, .passive, .active, .active, .frozen]
            )

            StreakView(
                isNewStreakRecord: false,
                currentStreakCountString: "0 days",
                daysStates: [.passive, .passive, .active, .passive, .passive]
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
