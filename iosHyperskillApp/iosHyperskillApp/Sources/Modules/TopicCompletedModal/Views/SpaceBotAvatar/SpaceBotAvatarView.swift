import SwiftUI

struct SpaceBotAvatarView: View {
    let imageResource: ImageResource

    var body: some View {
        Image(imageResource)
            .renderingMode(.original)
            .resizable()
            .scaledToFit()
            .clipShape(Circle())
            .frame(widthHeight: DeviceInfo.current.isPad ? 344 : 228)
    }
}

#if DEBUG
#Preview {
    ScrollView {
        LazyVStack {
            ForEach(
                Array(SpaceBotAvatarImageResources.resources.enumerated()),
                id: \.offset
            ) { index, resource in
                HStack {
                    Text("\(index + 1)")
                    SpaceBotAvatarView(imageResource: resource)
                }
            }
        }
        .padding()
    }
}
#endif
