import SwiftUI

struct StepQuizMatchingView: View {
    @ObservedObject var viewModel: StepQuizMatchingViewModel

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            ForEach(Array(viewModel.viewData.items.enumerated()), id: \.element.option) { index, item in
                StepQuizMatchingItemView(
                    title: item.title.text,
                    option: item.option.text,
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
