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

                    Text("\(currentStreak)")
                        .foregroundColor(.primaryText)
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
