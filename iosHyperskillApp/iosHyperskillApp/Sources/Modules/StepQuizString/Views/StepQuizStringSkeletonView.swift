import SwiftUI

extension StepQuizStringSkeletonView {
    struct Appearance {
        let smallSkeletonHeight: CGFloat = 48
        let largeSkeletonHeight: CGFloat = 96
    }
}

struct StepQuizStringSkeletonView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        VStack(spacing: LayoutInsets.largeInset) {
            Skeleton()
                .frame(height: appearance.largeSkeletonHeight)

            Skeleton()
                .frame(height: appearance.smallSkeletonHeight)
        }
    }
}

struct StepQuizStringSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizStringSkeletonView()
            .padding()
            .previewLayout(.sizeThatFits)
    }
}
