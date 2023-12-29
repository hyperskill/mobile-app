import SwiftUI

extension StepQuizMatchingView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
        let itemHorizontalInset: CGFloat = 64
    }
}

struct StepQuizMatchingView: View {
    private(set) var appearance = Appearance()

    @ObservedObject var viewModel: StepQuizMatchingViewModel

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            ForEach(Array(viewModel.viewData.items.enumerated()), id: \.element.id) { index, item in
                switch item {
                case .title(let text):
                    HStack(spacing: 0) {
                        LatexView(
                            text: text,
                            configuration: .quizContent()
                        )
                        .padding()
                        .addBorder()

                        Spacer(minLength: appearance.itemHorizontalInset)
                    }
                case .option(let option):
                    HStack(spacing: 0) {
                        Spacer(minLength: appearance.itemHorizontalInset)

                        StepQuizSortingItemView(
                            text: option.text,
                            isMoveUpEnabled: index > 1,
                            isMoveDownEnabled: index < viewModel.viewData.items.count - 1,
                            onMoveUp: {
                                withAnimation {
                                    viewModel.doMoveUp(from: index)
                                }
                            },
                            onMoveDown: {
                                withAnimation {
                                    viewModel.doMoveDown(from: index)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

#if DEBUG
struct StepQuizMatchingView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizMatchingAssembly
            .makePlaceholder()
            .makeModule()
            .previewLayout(.sizeThatFits)
            .padding()
    }
}
#endif
