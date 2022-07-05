import SwiftUI

extension TrackAvatarView {
    struct Appearance {
        var imageWidthHeight: CGFloat = 20
        var containerWidthHeight: CGFloat = 34
    }
}

struct TrackAvatarView: View {
    private(set) var appearance = Appearance()

    let imageName: String

    var body: some View {
        Image(imageName)
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(widthHeight: appearance.imageWidthHeight)
            .frame(widthHeight: appearance.containerWidthHeight)
            .addBorder(cornerRadius: appearance.containerWidthHeight / 2)
    }
}

struct TrackAvatarView_Previews: PreviewProvider {
    static var previews: some View {
        TrackAvatarView(imageName: Images.TabBar.track)
            .previewLayout(.sizeThatFits)
            .padding()
    }
}
