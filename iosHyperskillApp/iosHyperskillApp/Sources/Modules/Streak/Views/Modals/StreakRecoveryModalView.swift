import SwiftUI

extension StreakRecoveryModalView {
    struct Appearance {
        let largeSpacing: CGFloat = 32
        let gemsBadgeWidthHeight: CGFloat = 36
        let warningBottomPadding: CGFloat = 4
    }
}

struct StreakRecoveryModalView: View {
    private(set) var appearance = Appearance()

    let recoveryPriceAmount: String
    let recoveryPriceLabel: String
    let modalText: String
    let restoreStreakButtonTapped: () -> Void
    let noThanksButtonTapped: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.largeSpacing) {
            Image(Images.Profile.Streak.RecoverModal.fire)

            VStack(alignment: .leading, spacing: LayoutInsets.largeInset) {
                Text(Strings.Streak.RecoverModal.title)
                    .foregroundColor(.primaryText)
                    .font(.title2)
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

                    Text("\(Text(recoveryPriceAmount).bold()) \(recoveryPriceLabel)")
                        .foregroundColor(.primaryText)
                        .font(.body)
                }
            }

            VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
                Text(Strings.Streak.RecoverModal.warning)
                    .foregroundColor(.disabledText)
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

struct StreakRecoveryModalView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StreakRecoveryModalView(
                recoveryPriceAmount: "25",
                recoveryPriceLabel: "gems",
                modalText: "Good to see you back! You used to study daily and had a N-day streak. Great job!",
                restoreStreakButtonTapped: {},
                noThanksButtonTapped: {}
            )

            StreakRecoveryModalView(
                recoveryPriceAmount: "25",
                recoveryPriceLabel: "gems",
                modalText: "Good to see you back! You used to study daily and had a N-day streak. Great job!",
                restoreStreakButtonTapped: {},
                noThanksButtonTapped: {}
            )
            .preferredColorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
    }
}
