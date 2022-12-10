import SwiftUI

struct TrackHeaderSkeletonView: View {
    private static let headerViewAppearance = TrackHeaderView.Appearance()

    var body: some View {
        VStack(alignment: .center, spacing: LayoutInsets.defaultInset) {
            SkeletonCircleView(
                appearance: .init(
                    size: CGSize(
                        width: Self.headerViewAppearance.avatarImageWidthHeight,
                        height: Self.headerViewAppearance.avatarImageWidthHeight
                    )
                )
            )

            VStack(spacing: LayoutInsets.smallInset) {
                SkeletonRoundedView()
                    .frame(width: 200, height: 24)

                SkeletonRoundedView()
                    .frame(width: 100, height: 17)
            }
        }
        .padding(.horizontal)
    }
}

struct TrackHeaderSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        TrackHeaderSkeletonView()
    }
}
