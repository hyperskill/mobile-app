import shared
import SwiftUI

struct SettingsView: View {
    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("AUTH")) {
                    Text(Settings.default.getStringOrNull(key: AuthCacheKeyValues.shared.AUTH_RESPONSE) ?? "")
                }
            }
            .navigationBarTitle("SettingsTitle")
        }
    }
}

struct SettingsView_Previews: PreviewProvider {
    static var previews: some View {
        SettingsView()
    }
}
