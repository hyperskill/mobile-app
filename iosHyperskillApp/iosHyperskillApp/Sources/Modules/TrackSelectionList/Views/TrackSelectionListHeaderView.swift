import SwiftUI

extension TrackSelectionListHeaderView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset

        let avatarSize = CGSize(width: 40, height: 40)
        let avatarImageSize = CGSize(width: 28, height: 28)
    }
}

struct TrackSelectionListHeaderView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        VStack(spacing: appearance.spacing) {
            BrandLinearGradient()
                .mask(
                    Image(Images.Track.trackFilled)
                        .resizable()
                        .renderingMode(.template)
                        .aspectRatio(contentMode: .fit)
                        .frame(size: appearance.avatarImageSize)
                )
                .frame(size: appearance.avatarSize)

            Text(Strings.Auth.NewUserPlaceholder.title)
                .bold()
                .font(.title2)
                .foregroundColor(.primaryText)
                .multilineTextAlignment(.center)

            Text(Strings.Auth.NewUserPlaceholder.subtitle)
                .font(.body)
                .foregroundColor(.primaryText)
                .multilineTextAlignment(.center)
        }
    }
}

struct TrackSelectionListHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        TrackSelectionListHeaderView()
            .padding()

        TrackSelectionListHeaderView()
            .padding()
            .preferredColorScheme(.dark)
    }
}
