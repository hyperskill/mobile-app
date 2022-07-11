import SwiftUI

struct TrackHeaderView: View {
    let iconImageName: String

    let title: String

    let subtitle: String

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            TrackAvatarView(imageName: iconImageName)

            VStack(spacing: LayoutInsets.smallInset) {
                Text(title)
                    .font(.title3)
                    .foregroundColor(.primaryText)
                    .multilineTextAlignment(.center)

                Text(subtitle)
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
            title: "Python for Beginners",
            subtitle: "Learning now"
        )
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
