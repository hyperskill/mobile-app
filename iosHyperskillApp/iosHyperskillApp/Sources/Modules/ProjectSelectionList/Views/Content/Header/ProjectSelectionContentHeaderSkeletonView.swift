import SwiftUI

struct ProjectSelectionContentHeaderSkeletonView: View {
    private static let headerViewAppearance = ProjectSelectionContentHeaderView.Appearance()

    var body: some View {
        VStack(spacing: Self.headerViewAppearance.spacing) {
            SkeletonCircleView(
                appearance: .init(size: Self.headerViewAppearance.avatarSize)
            )

            SkeletonRoundedView()
                .frame(height: 24)

            SkeletonRoundedView()
                .frame(width: 200, height: 17)
        }
        .frame(maxWidth: .infinity)
    }
}

struct ProjectSelectionContentHeaderSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectSelectionContentHeaderSkeletonView()
            .padding()
    }
}
