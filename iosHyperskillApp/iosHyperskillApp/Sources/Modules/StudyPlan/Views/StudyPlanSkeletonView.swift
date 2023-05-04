import SwiftUI

extension StudyPlanSkeletonView {
    struct Appearance {
        let skeletonHeight: CGFloat = 110
    }
}

struct StudyPlanSkeletonView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        ScrollView([], showsIndicators: false) {
            VStack(spacing: LayoutInsets.largeInset) {
                ForEach(0..<4) { _ in
                    SkeletonRoundedView()
                        .frame(height: appearance.skeletonHeight)
                }
            }
            .padding([.horizontal, .bottom])
        }
    }
}

struct StudyPlanSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        StudyPlanSkeletonView()
    }
}
