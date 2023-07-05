import SwiftUI

extension ProjectLevelAvatarView {
    struct Appearance {
        var imageSize = CGSize(width: 16, height: 16)

        var padding = LayoutInsets.small

        let borderColor = Color.border
        let borderWidth: CGFloat = 1

        fileprivate var cornerRadius: CGFloat {
            max(imageSize.width, imageSize.height)
        }
    }
}

struct ProjectLevelAvatarView: View {
    private(set) var appearance = Appearance()

    let level: SharedProjectLevelWrapper

    var body: some View {
        Image(level.iconImageName)
            .resizable()
            .renderingMode(.original)
            .aspectRatio(contentMode: .fit)
            .frame(size: appearance.imageSize)
            .padding(appearance.padding.edgeInsets)
            .overlay(
                RoundedRectangle(
                    cornerRadius: appearance.cornerRadius
                )
                .stroke(
                    appearance.borderColor,
                    lineWidth: appearance.borderWidth
                )
            )
    }
}

struct ProjectLevelAvatarView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            ProjectLevelAvatarView(level: .easy)
            ProjectLevelAvatarView(level: .medium)
            ProjectLevelAvatarView(level: .hard)
            ProjectLevelAvatarView(level: .nightmare)
        }
        .padding()
    }
}
