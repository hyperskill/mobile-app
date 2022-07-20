import SwiftUI

extension StepQuizTableSkeletonView {
    struct Appearance {
        let skeletonHeight: CGFloat = 21
    }
}

struct StepQuizTableSkeletonView: View {
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

struct StepQuizTableSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizTableSkeletonView()
            .padding()
            .previewLayout(.sizeThatFits)
    }
}
