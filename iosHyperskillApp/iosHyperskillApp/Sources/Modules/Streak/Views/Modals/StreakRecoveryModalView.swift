import SwiftUI

extension StreakRecoveryModalView {
    struct Appearance {
        let largeSpacing: CGFloat = 32
        let gemsBadgeIconSize = CGSize(width: 36, height: 36)
        let warningBottomPadding: CGFloat = 4
    }
}

struct StreakRecoveryModalView: View {
    private(set) var appearance = Appearance()

    let recoveryPriceAmount: String
    let recoveryPriceLabel: String
    let modalText: String
    let isFirstTimeOffer: Bool
    let nextRecoveryPriceText: String?

    let restoreStreakButtonTapped: () -> Void
    let noThanksButtonTapped: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.largeSpacing) {
            Image(Images.Profile.Streak.RecoverModal.fire)
                .renderingMode(.original)
                .aspectRatio(contentMode: .fit)
                .frame(maxWidth: .infinity, alignment: .leading)

            VStack(alignment: .leading, spacing: LayoutInsets.largeInset) {
                Text(Strings.Streak.RecoverModal.title)
                    .foregroundColor(.primaryText)
                    .font(.title2)
                    .bold()

                Text(modalText)
                    .foregroundColor(.primaryText)
                    .font(.body)

                HStack(spacing: LayoutInsets.smallInset) {
                    HypercoinLabel(
                        title: {
                            Text("\(Text(recoveryPriceAmount).bold()) \(recoveryPriceLabel)")
                                .font(.body)
                                .foregroundColor(.primaryText)
                        },
                        appearance: .init(iconSize: appearance.gemsBadgeIconSize)
                    )

                    if isFirstTimeOffer {
                        BadgeView.firstTimeOffer()
                    }
                }

                if let nextRecoveryPriceText {
                    Text(nextRecoveryPriceText)
                        .foregroundColor(.tertiaryText)
                        .font(.body)
                }
            }

            VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
                Text(Strings.Streak.RecoverModal.warning)
                    .foregroundColor(.tertiaryText)
                    .font(.body)
                    .padding(.bottom, appearance.warningBottomPadding)

                Button(Strings.Streak.RecoverModal.restoreStreak) {
                    restoreStreakButtonTapped()
                }
                .buttonStyle(RoundedRectangleButtonStyle(style: .violet))

                Button(Strings.Streak.RecoverModal.noThanks) {
                    noThanksButtonTapped()
                }
                .buttonStyle(OutlineButtonStyle())
            }
        }
        .padding([.horizontal, .bottom])
    }
}

#Preview {
    StreakRecoveryModalView(
        recoveryPriceAmount: "25",
        recoveryPriceLabel: "gems",
        modalText: "Good to see you back! You used to study daily and had a N-day streak. Great job!",
        isFirstTimeOffer: false,
        nextRecoveryPriceText: nil,
        restoreStreakButtonTapped: {},
        noThanksButtonTapped: {}
    )
}

#Preview {
    StreakRecoveryModalView(
        recoveryPriceAmount: "0",
        recoveryPriceLabel: "gems",
        modalText: "Good to see you back! You used to study daily and had a N-day streak. Great job!",
        isFirstTimeOffer: true,
        nextRecoveryPriceText: "Then for 25 gems",
        restoreStreakButtonTapped: {},
        noThanksButtonTapped: {}
    )
}
