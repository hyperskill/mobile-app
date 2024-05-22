import SwiftUI

extension SpaceBotAvatarView {
    struct Appearance {
        var size: CGSize {
            DeviceInfo.current.isPad ? CGSize(width: 344, height: 344) : CGSize(width: 228, height: 228)
        }
    }
}

struct SpaceBotAvatarView: View {
    private(set) var appearance = Appearance()

    let imageResource: ImageResource

    var body: some View {
        Image(imageResource)
            .renderingMode(.original)
            .resizable()
            .scaledToFit()
            .clipShape(Circle())
            .frame(size: appearance.size)
    }
}

#if DEBUG
#Preview {
    ScrollView {
        LazyVStack {
            ForEach(SpaceBotAvatarImageResources.resources, id: \.self) { resource in
                SpaceBotAvatarView(imageResource: resource)
            }
        }
        .padding()
    }
}
#endif
