import SwiftUI

struct ProjectSelectionListHeaderSkeletonView: View {
    private static let headerViewAppearance = ProjectSelectionListHeaderView.Appearance()

    var body: some View {
        VStack(spacing: Self.headerViewAppearance.spacing) {
            SkeletonCircleView(
                appearance: .init(size: Self.headerViewAppearance.avatarSize)
            )

            SkeletonRoundedView()
                .frame(height: 24)
                .frame(maxWidth: 400)

            SkeletonRoundedView()
                .frame(width: 200, height: 17)
        }
        .frame(maxWidth: .infinity)
    }
}

struct ProjectSelectionListHeaderSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectSelectionListHeaderSkeletonView()
            .padding()
    }
}
