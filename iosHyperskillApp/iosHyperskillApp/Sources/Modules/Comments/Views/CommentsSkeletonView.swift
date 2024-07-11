import SwiftUI

struct CommentsSkeletonView: View {
    var body: some View {
        ScrollView([], showsIndicators: false) {
            VStack {
                ForEach(0..<10) { index in
                    CommentsCommentSkeletonView(
                        textHeight: index.isMultiple(of: 2) ? 88 : 132
                    )
                    Divider()
                        .padding(.vertical)
                }
            }
            .padding()
        }
        .frame(maxWidth: .infinity)
        .edgesIgnoringSafeArea(.bottom)
    }
}

struct CommentsCommentSkeletonView: View {
    var textHeight: CGFloat = 88

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            HStack {
                SkeletonCircleView(
                    appearance: .init(
                        size: CommentsCommentView.Appearance().avatarSize
                    )
                )

                VStack(alignment: .leading) {
                    SkeletonRoundedView()
                        .frame(height: 20)

                    SkeletonRoundedView()
                        .frame(height: 20)
                }
            }

            SkeletonRoundedView()
                .frame(height: textHeight)
        }
    }
}

#if DEBUG
#Preview {
    CommentsSkeletonView()
}
#endif
