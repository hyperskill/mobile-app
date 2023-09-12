import SwiftUI

struct StepQuizCodeSkeletonView: View {
    var body: some View {
        VStack(spacing: LayoutInsets.largeInset) {
            SkeletonRoundedView()
                .frame(height: 52)
            SkeletonRoundedView()
                .frame(height: 146)
        }
    }
}

struct StepQuizCodeSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizCodeSkeletonView()
            .padding()
            .previewLayout(.sizeThatFits)
    }
}
