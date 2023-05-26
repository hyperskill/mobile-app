import SwiftUI

struct ProjectSelectionDetailsSkeletonView: View {
    private let contentViewAppearance = ProjectSelectionDetailsContentView.Appearance()

    var body: some View {
        ScrollView([], showsIndicators: false) {
            VStack(spacing: contentViewAppearance.spacing) {
                SkeletonRoundedView()
                    .frame(height: 240)

                SkeletonRoundedView()
                    .frame(height: 192)

                SkeletonRoundedView()
                    .frame(height: 78)

                SkeletonRoundedView()
                    .frame(height: 44)
            }
            .padding()
        }
        .frame(maxWidth: .infinity)
        .edgesIgnoringSafeArea(.bottom)
    }
}

struct ProjectSelectionDetailsSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            ProjectSelectionDetailsSkeletonView()
                .navigationTitle(Strings.TrackSelectionList.title)
        }
    }
}
