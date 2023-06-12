import SwiftUI

struct StreakRecoverySheetView: View {
    let recoveryPrice: Int
    let previousStreak: Int

    var body: some View {
        Text("Recovery price: \(recoveryPrice), previous streak: \(previousStreak)")
    }
}

struct StreakRecoverySheetView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StreakRecoverySheetView(
                recoveryPrice: 25,
                previousStreak: 2
            )

            StreakRecoverySheetView(
                recoveryPrice: 25,
                previousStreak: 2
            )
            .preferredColorScheme(.dark)
        }
        .padding()
        .previewLayout(.sizeThatFits)


    }
}
