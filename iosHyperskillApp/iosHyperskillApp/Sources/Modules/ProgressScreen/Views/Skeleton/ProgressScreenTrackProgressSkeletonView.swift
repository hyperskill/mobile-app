import SwiftUI

extension ProgressScreenTrackProgressSkeletonView {
    struct Appearance {
        let spacing: CGFloat
        let interitemSpacing: CGFloat
    }
}

struct ProgressScreenTrackProgressSkeletonView: View {
    let appearance: Appearance

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            ProgressScreenSectionTitleSkeletonView(
                appearance: .init(spacing: appearance.spacing)
            )

            VStack(spacing: appearance.interitemSpacing) {
                ForEach(0..<2) { _ in
                    ProgressScreenCardSkeletonView()
                }

                HStack(spacing: appearance.interitemSpacing) {
                    ForEach(0..<2) { _ in
                        ProgressScreenCardSkeletonView()
                    }
                }
            }
        }
        .frame(maxWidth: .infinity)
    }
}

struct ProgressScreenTrackProgressSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        ProgressScreenTrackProgressSkeletonView(
            appearance: .init(
                spacing: LayoutInsets.defaultInset,
                interitemSpacing: LayoutInsets.smallInset
            )
        )
        .padding()
    }
}
