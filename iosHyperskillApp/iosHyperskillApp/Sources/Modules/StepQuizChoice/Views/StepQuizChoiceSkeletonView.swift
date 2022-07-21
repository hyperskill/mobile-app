import SwiftUI

extension StepQuizChoiceSkeletonView {
    struct Appearance {
        let smallSkeletonHeight: CGFloat = 21
        let largeSkeletonHeight: CGFloat = 48
    }
}

struct StepQuizChoiceSkeletonView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        VStack(spacing: LayoutInsets.largeInset) {
            ForEach(0..<4) { _ in
                SkeletonRoundedView()
                    .frame(height: appearance.smallSkeletonHeight)
            }

            SkeletonRoundedButton()
        }
    }
}

struct StepQuizChoiceSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizChoiceSkeletonView()
            .padding()
            .previewLayout(.sizeThatFits)
    }
}
