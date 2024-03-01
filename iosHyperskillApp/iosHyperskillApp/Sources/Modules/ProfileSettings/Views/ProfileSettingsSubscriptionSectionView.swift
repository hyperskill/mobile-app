import SwiftUI

struct ProfileSettingsSubscriptionSectionView: View {
    let description: String

    let action: () -> Void

    var body: some View {
        Section(header: Text(Strings.Settings.subscription)) {
            Button(
                action: { action() },
                label: {
                    HStack(spacing: 0) {
                        Text(description)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .layoutPriority(1)
                        Spacer()
                        NavigationLink.empty
                    }
                }
            )
            .accentColor(.primaryText)
        }
    }
}

#Preview {
    NavigationView {
        Form {
            ProfileSettingsSubscriptionSectionView(
                description: "Try Mobile only plan for $12.00",
                action: {}
            )

            ProfileSettingsSubscriptionSectionView(
                description: "Mobile only",
                action: {}
            )
        }
    }
}
