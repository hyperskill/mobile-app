import SwiftUI

extension StepQuizMatchingSkeletonView {
    struct Appearance {
        let smallSkeletonHeight: CGFloat = 60
        let largeSkeletonHeight: CGFloat = 104
        let itemHorizontalInset: CGFloat = 64
    }
}

struct StepQuizMatchingSkeletonView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.largeInset) {
            ForEach(0..<2) { _ in
                VStack(alignment: .leading, spacing: LayoutInsets.largeInset) {
                    HStack(spacing: 0) {
                        SkeletonRoundedView()
                            .frame(height: appearance.smallSkeletonHeight)

                        Spacer(minLength: appearance.itemHorizontalInset)
                    }

                    HStack(spacing: 0) {
                        Spacer(minLength: appearance.itemHorizontalInset)

                        SkeletonRoundedView()
                            .frame(height: appearance.largeSkeletonHeight)
                    }
                }
            }

            SkeletonRoundedButton()
        }
    }
}

struct StepQuizMatchingSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizMatchingSkeletonView()
            .padding()
            .previewLayout(.sizeThatFits)
    }
}
