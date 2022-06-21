import SwiftUI

struct ProfileView: View {
    @State private var presentingSettings = false

    var body: some View {
        NavigationView {
            Text(
                ""
            )
            .navigationTitle(Strings.Profile.title)
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(
                        action: { presentingSettings = true },
                        label: { Image(systemName: "gear") }
                    )
                }
            }
            .sheet(isPresented: $presentingSettings) {
                SettingsAssembly().makeModule()
            }
        }
    }
}

struct ProfileView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileView()
    }
}
