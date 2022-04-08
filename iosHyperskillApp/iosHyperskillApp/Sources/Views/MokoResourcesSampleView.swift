import shared
import SwiftUI

struct MokoResourcesSampleView: View {
    var body: some View {
        VStack {
            Text(SharedResources.strings.shared.auth_log_in_title.localized())
            Circle()
                .fill(Color(SharedResources.colors.shared.color_background.dynamicUIColor))
                .frame(width: 100, height: 100)
        }
    }
}

struct MokoResourcesSampleView_Previews: PreviewProvider {
    static var previews: some View {
        MokoResourcesSampleView()
    }
}
