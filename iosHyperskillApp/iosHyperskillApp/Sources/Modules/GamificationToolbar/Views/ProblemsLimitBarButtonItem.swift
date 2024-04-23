import SwiftUI

struct ProblemsLimitBarButtonItem: View {
    let text: String

    let onTap: () -> Void

    var body: some View {
        Button(
            action: onTap,
            label: {
                HStack {
                    Image(.navigationBarProblemsLimit)
                        .renderingMode(.original)
                        .resizable()
                        .aspectRatio(contentMode: .fit)

                    Text(text)
                        .foregroundColor(.primaryText)
                        .animation(.default, value: text)
                }
            }
        )
    }
}

#if DEBUG
#Preview {
    NavigationView {
        Text("Hello, World!")
            .navigationTitle("Navigation")
            .toolbar {
                ToolbarItem(placement: .primaryAction) {
                    ProblemsLimitBarButtonItem(text: "10", onTap: {})
                }
            }
    }
}
#endif
