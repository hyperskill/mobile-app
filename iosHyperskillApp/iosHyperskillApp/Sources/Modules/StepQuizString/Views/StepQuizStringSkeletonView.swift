import SwiftUI

struct StepQuizStringSkeletonView: View {
    var body: some View {
        VStack(spacing: LayoutInsets.largeInset) {
            SkeletonRoundedView()
                .frame(height: 96)

            SkeletonRoundedButton()
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
