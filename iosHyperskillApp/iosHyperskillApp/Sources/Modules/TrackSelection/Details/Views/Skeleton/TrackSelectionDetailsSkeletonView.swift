import SwiftUI

struct TrackSelectionDetailsSkeletonView: View {
    private let contentViewAppearance = TrackSelectionDetailsContentView.Appearance()

    var body: some View {
        ScrollView([], showsIndicators: false) {
            VStack(spacing: contentViewAppearance.spacing) {
                SkeletonRoundedView()
                    .frame(height: 96)

                SkeletonRoundedView()
                    .frame(height: 192)

                SkeletonRoundedView()
                    .frame(height: 240)

                SkeletonRoundedView()
                    .frame(height: 44)
            }
            .padding()
        }
        .frame(maxWidth: .infinity)
        .edgesIgnoringSafeArea(.bottom)
    }
}

struct TrackSelectionDetailsSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            TrackSelectionDetailsSkeletonView()
                .navigationTitle(Strings.TrackSelectionList.title)
        }
    }
}
