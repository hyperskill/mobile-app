import SwiftUI

struct TrackHeaderView: View {
    let iconImageName: String

    let name: String

    let role: String

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            TrackAvatarView(imageName: iconImageName)

            VStack(spacing: LayoutInsets.smallInset) {
                Text(name)
                    .font(.title3)
                    .foregroundColor(.primaryText)
                    .multilineTextAlignment(.center)

                Text(role)
                    .font(.caption)
                    .foregroundColor(.secondaryText)
                    .multilineTextAlignment(.center)
            }
        }
        .padding(.horizontal)
    }
}

struct TrackHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        TrackHeaderView(
            iconImageName: Images.TabBar.track,
            name: "Python for Beginners",
            role: "Learning now"
        )
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
