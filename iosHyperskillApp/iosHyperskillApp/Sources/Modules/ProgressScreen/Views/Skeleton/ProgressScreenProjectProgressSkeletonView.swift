import SwiftUI

extension ProgressScreenProjectProgressSkeletonView {
    struct Appearance {
        let spacing: CGFloat
        let interitemSpacing: CGFloat
    }
}

struct ProgressScreenProjectProgressSkeletonView: View {
    let appearance: Appearance

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            ProgressScreenSectionTitleSkeletonView(
                appearance: .init(spacing: appearance.spacing)
            )

            HStack(spacing: appearance.interitemSpacing) {
                ForEach(0..<2) { _ in
                    ProgressScreenCardSkeletonView()
                }
            }
        }
        .frame(maxWidth: .infinity)
    }
}

struct ProgressScreenProjectProgressSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        ProgressScreenProjectProgressSkeletonView(
            appearance: .init(
                spacing: LayoutInsets.defaultInset,
                interitemSpacing: LayoutInsets.smallInset
            )
        )
        .padding()
    }
}
