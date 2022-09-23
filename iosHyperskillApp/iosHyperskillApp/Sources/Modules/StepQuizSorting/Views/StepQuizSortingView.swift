import SwiftUI

struct StepQuizSortingView: View {
    @StateObject var viewModel: StepQuizSortingViewModel

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            ForEach(Array(viewModel.viewData.items.enumerated()), id: \.element) { index, item in
                StepQuizSortingItemView(
                    text: item.text,
                    isMoveUpEnabled: index > 0,
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

#if DEBUG
struct StepQuizSortingView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizSortingAssembly
            .makePlaceholder()
            .makeModule()
            .previewLayout(.sizeThatFits)
            .padding()
    }
}
#endif
