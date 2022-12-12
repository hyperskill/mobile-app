import SwiftUI

struct StreakBarButtonItem: View {
    let currentStreak: Int

    let onTap: () -> Void

    var body: some View {
        Button(
            action: onTap,
            label: {
                HStack {
                    Image(Images.NavigationBar.streak)
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
                        StreakBarButtonItem(currentStreak: 3, onTap: {})
                    }
                }
        }
    }
}
