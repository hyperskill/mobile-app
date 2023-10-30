import SwiftUI

extension StepQuizFillBlanksSkeletonView {
    struct Appearance {
        let blankSize = FillBlanksSelectCollectionViewCell.Appearance.defaultSize

        let textHeight: CGFloat = 22
        let textWidthSmall: CGFloat = 35
        let textWidthDefault: CGFloat = 50
        let textWidthLarge: CGFloat = 100
        let textWidthExtraLarge: CGFloat = 150

        let spacing = LayoutInsets.defaultInset
        let interitemSpacing = LayoutInsets.smallInset
    }
}

struct StepQuizFillBlanksSkeletonView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        VStack(spacing: 0) {
            FillBlanksQuizTitleViewWrapper()
            content
            Divider()
        }
        .padding(.horizontal, -LayoutInsets.defaultInset)
    }

    private var content: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            HStack(spacing: appearance.interitemSpacing) {
                textView
                    .frame(width: appearance.textWidthDefault)
                blankView
                textView
                    .frame(width: appearance.textWidthSmall)
                textView
                    .frame(width: appearance.textWidthLarge)
                textView
                    .frame(width: appearance.textWidthSmall)
            }

            HStack(spacing: appearance.interitemSpacing) {
                textView
                    .frame(width: appearance.textWidthLarge)
                textView
                    .frame(width: appearance.textWidthSmall)
                textView
                    .frame(width: appearance.textWidthLarge)
                blankView
            }

            HStack(spacing: appearance.interitemSpacing) {
                blankView
                textView
                    .frame(width: appearance.textWidthSmall)
                textView
                    .frame(width: appearance.textWidthExtraLarge)
                textView
                    .frame(width: appearance.textWidthSmall)
                Spacer()
            }
        }
        .padding()
        .background(BackgroundView())
    }

    private var textView: some View {
        SkeletonRoundedView()
            .frame(height: appearance.textHeight)
    }

    private var blankView: some View {
        Color.clear
            .frame(size: appearance.blankSize)
            .addBorder()
    }
}

#Preview {
    StepQuizFillBlanksSkeletonView()
        .padding()
}

// MARK: - FillBlanksQuizTitleViewWrapper -

private struct FillBlanksQuizTitleViewWrapper: UIViewRepresentable {
    func makeUIView(context: Context) -> FillBlanksQuizTitleView { FillBlanksQuizTitleView() }

    func updateUIView(_ uiView: FillBlanksQuizTitleView, context: Context) {}
}
