import SwiftUI

extension ProgressScreenSectionTitleSkeletonView {
    struct Appearance {
        let spacing: CGFloat

        let avatarSize = ProgressScreenTrackProgressContentView.Appearance(
            spacing: 0,
            interitemSpacing: 0,
            cardBackgroundColor: .clear,
            cardCornerRadius: 0
        ).avatarSize
    }
}

struct ProgressScreenSectionTitleSkeletonView: View {
    let appearance: Appearance

    var body: some View {
        HStack(spacing: appearance.spacing) {
            SkeletonCircleView(appearance: .init(size: appearance.avatarSize))

            SkeletonRoundedView()
                .frame(height: appearance.avatarSize.height)
        }
    }
}

struct ProgressScreenSectionTitleSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        ProgressScreenSectionTitleSkeletonView(
            appearance: .init(spacing: LayoutInsets.defaultInset)
        )
        .padding()
    }
}
