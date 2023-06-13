import SwiftUI

extension StreakRecoveryModalView {
    struct Appearance {
        let largeSpacing: CGFloat = 32
        let gemsBadgeWidthHeight: CGFloat = 36
    }
}

struct StreakRecoveryModalView: View {
    private(set) var appearance = Appearance()

    let recoveryPriceAmount: String
    let recoveryPriceLabel: String
    let modalText: String

    weak var streakRecoveryModalDelegate: StreakRecoveryModalDelegate?

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.largeSpacing) {
            Image(Images.Profile.Streak.RecoverModal.fire)

            VStack(alignment: .leading, spacing: LayoutInsets.largeInset) {
                Text(Strings.Streak.RecoverModal.title)
                    .foregroundColor(.primaryText)
                    .font(.title3)
                    .bold()

                Text(modalText)
                    .foregroundColor(.primaryText)
                    .font(.body)

                HStack(alignment: .center, spacing: LayoutInsets.smallInset) {
                    Image(Images.StepQuiz.ProblemOfDaySolvedModal.gemsBadge)
                        .renderingMode(.original)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(widthHeight: appearance.gemsBadgeWidthHeight)

                    Text(recoveryPriceAmount)
                        .foregroundColor(.primaryText)
                        .font(.body)
                        .bold()

                    Text(recoveryPriceLabel)
                        .foregroundColor(.primaryText)
                        .font(.body)
                }
            }

            VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
                Text(Strings.Streak.RecoverModal.warning)
                    .foregroundColor(.secondaryText)
                    .font(.body)

                Button(Strings.Streak.RecoverModal.restoreStreak) {
                    streakRecoveryModalDelegate?.streakRecoveryModalDidTapRestoreStreakButton()
                }
                .buttonStyle(RoundedRectangleButtonStyle(style: .violet))

                Button(Strings.Streak.RecoverModal.noThanks) {
                    streakRecoveryModalDelegate?.streakRecoveryModalDidTapNoThanksButton()
                }
                .buttonStyle(OutlineButtonStyle())
            }
        }
        .padding([.horizontal, .bottom])
        .onAppear {
            streakRecoveryModalDelegate?.streakRecoveryModalDidAppear()
        }
        .onDisappear {
            streakRecoveryModalDelegate?.streakRecoveryModalDidDisappear()
        }
    }
}

struct StreakRecoveryModalView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StreakRecoveryModalView(
                recoveryPriceAmount: "25",
                recoveryPriceLabel: "gems",
                modalText: "Good to see you back! You used to study daily and had a N-day streak. Great job!"
            )

            StreakRecoveryModalView(
                recoveryPriceAmount: "25",
                recoveryPriceLabel: "gems",
                modalText: "Good to see you back! You used to study daily and had a N-day streak. Great job!"
            )
            .preferredColorScheme(.dark)
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
