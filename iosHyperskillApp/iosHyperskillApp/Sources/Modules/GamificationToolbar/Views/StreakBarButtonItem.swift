import SwiftUI

struct StreakBarButtonItem: View {
    let currentStreak: Int

    let isCompletedToday: Bool

    let onTap: () -> Void

    var body: some View {
        Button(
            action: onTap,
            label: {
                HStack {
                    Image(
                        isCompletedToday
                            ? Images.NavigationBar.streakCompleted
                            : Images.NavigationBar.streakUncompleted
                    )
                    .renderingMode(.original)
                    .resizable()
                    .aspectRatio(contentMode: .fit)

                    if #available(iOS 17.0, *) {
                        Text("\(currentStreak)")
                            .foregroundColor(.primaryText)
                            .animation(.default, value: currentStreak)
                            .contentTransition(.numericText(value: Double(currentStreak)))
                    } else {
                        Text("\(currentStreak)")
                            .foregroundColor(.primaryText)
                            .animation(.default, value: currentStreak)
                    }
                }
            }
        )
    }
}

struct StreakBarButtonItem_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            Text("Hello, World!")
                .navigationTitle("Navigation")
                .toolbar {
                    ToolbarItem(placement: .primaryAction) {
                        StreakBarButtonItem(
                            currentStreak: 3,
                            isCompletedToday: true,
                            onTap: {}
                        )
                    }
                }
        }
    }
}
