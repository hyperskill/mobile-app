import SwiftUI

extension StepQuizTableSkeletonView {
    struct Appearance {
        let skeletonHeight: CGFloat = 44
    }
}

struct StepQuizTableSkeletonView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        VStack(spacing: LayoutInsets.largeInset) {
            ForEach(0..<4) { _ in
                SkeletonRoundedView()
                    .frame(height: appearance.skeletonHeight)
            }
        }
    }
}

#if DEBUG
#Preview {
    StepQuizTableSkeletonView()
        .padding()
}
#endif
