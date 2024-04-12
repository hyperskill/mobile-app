import SwiftUI

struct StepQuizSortingView: View {
    @ObservedObject var viewModel: StepQuizSortingViewModel

    @Environment(\.isEnabled) private var isEnabled

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
        .opacity(isEnabled ? 1 : 0.5)
        .animation(.easeInOut(duration: 0.33), value: isEnabled)
    }
}

#if DEBUG
#Preview {
    ScrollView {
        VStack(spacing: LayoutInsets.defaultInset) {
            StepQuizSortingAssembly
                .makePlaceholder()
                .makeModule()

            Divider()

            StepQuizSortingAssembly
                .makePlaceholder()
                .makeModule()
                .disabled(true)
        }
        .padding()
    }
}
#endif
