import SwiftUI

extension StepQuizSortingSkeletonView {
    struct Appearance {
        let skeletonHeight: CGFloat = 100
    }
}

struct StepQuizSortingSkeletonView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        VStack(spacing: LayoutInsets.largeInset) {
            ForEach(0..<4) { _ in
                Skeleton()
                    .frame(height: appearance.skeletonHeight)
            }
        }
    }
}

struct StepQuizSortingSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizSortingSkeletonView()
            .padding()
            .previewLayout(.sizeThatFits)
    }
}
