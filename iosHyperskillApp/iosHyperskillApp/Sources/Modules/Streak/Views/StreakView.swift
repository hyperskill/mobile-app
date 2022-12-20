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

        let getStreakFreezeButtonMinHeight: CGFloat = 32
        let streakFreezeIconWidthHeight: CGFloat = 28
    }
}

struct StreakView: View {
    private(set) var appearance = Appearance()

    let isNewStreakRecord: Bool
    let currentStreakCountString: String

    let daysStates: [StreakDayState]

    let streakFreezeState: ProfileFeatureStreakFreezeStateKs?

    let onStreakFreezeTapped: () -> Void

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
                        Image(Images.Profile.Streak.crown)
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

            if let streakFreezeState {
                switch streakFreezeState {
                case .notEnoughGems, .canBuy:
                    Button(Strings.Streak.getOneDayStreakFreeze) {
                        onStreakFreezeTapped()
                    }
                    .buttonStyle(OutlineButtonStyle(
                        minHeight: appearance.getStreakFreezeButtonMinHeight,
                        maxWidth: nil
                    ))
                    .buttonStyle(BounceButtonStyle())
                case .alreadyHave:
                    Button(
                        action: onStreakFreezeTapped,
                        label: {
                            HStack(spacing: LayoutInsets.smallInset) {
                                Image(Images.Profile.Streak.FreezeModal.snowflakeBadge)
                                    .renderingMode(.original)
                                    .resizable()
                                    .frame(widthHeight: appearance.streakFreezeIconWidthHeight)

                                Text(Strings.Streak.youHaveOneDayStreakFreeze)
                            }
                        }
                    )
                }
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
                daysStates: [.passive, .passive, .active, .active, .frozen],
                streakFreezeState: .alreadyHave,
                onStreakFreezeTapped: {}
            )

            StreakView(
                isNewStreakRecord: false,
                currentStreakCountString: "0 days",
                daysStates: [.passive, .passive, .active, .passive, .passive],
                streakFreezeState: .alreadyHave,
                onStreakFreezeTapped: {}
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
