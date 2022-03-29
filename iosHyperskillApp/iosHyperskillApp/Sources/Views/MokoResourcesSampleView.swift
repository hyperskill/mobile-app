import shared
import SwiftUI

struct MokoResourcesSampleView: View {
    var body: some View {
        VStack {
            Circle()
                .fill(Color(SharedResources.colors().colorAccentSharedAlpha50.color.uiColor))
                .frame(width: 100, height: 100)
        }
    }
}

struct MokoResourcesSampleView_Previews: PreviewProvider {
    static var previews: some View {
        MokoResourcesSampleView()
    }
}
