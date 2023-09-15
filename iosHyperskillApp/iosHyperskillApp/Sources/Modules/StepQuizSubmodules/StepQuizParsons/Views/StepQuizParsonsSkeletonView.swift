import SwiftUI

extension StepQuizParsonsSkeletonView {
    struct Appearance {
        let skeletonHeight: CGFloat = 100
    }
}

struct StepQuizParsonsSkeletonView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        VStack(spacing: LayoutInsets.largeInset) {
            ForEach(0..<3) { _ in
                SkeletonRoundedView()
                    .frame(height: appearance.skeletonHeight)
            }

            SkeletonRoundedButton()
        }
    }
}

struct StepQuizParsonsSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizParsonsSkeletonView()
    }
}
