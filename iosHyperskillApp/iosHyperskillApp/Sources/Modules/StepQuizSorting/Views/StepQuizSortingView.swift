import SwiftUI

struct StepQuizSortingView: View {
    @State var viewData: StepQuizSortingViewData

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            ForEach(Array(viewData.items.enumerated()), id: \.element) { index, item in
                StepQuizSortingItemView(
                    text: item.text,
                    onMoveUp: index > 0 ? {
                        withAnimation {
                            doMoveUp(from: index)
                        }
                    } : nil,
                    onMoveDown: index < viewData.items.count - 1 ? {
                        withAnimation {
                            doMoveDown(from: index)
                        }
                    } : nil
                )
            }
        }
    }

    private func doMoveUp(from index: Int) {
        let tmp = viewData.items[index - 1]
        viewData.items[index - 1] = viewData.items[index]
        viewData.items[index] = tmp
    }

    private func doMoveDown(from index: Int) {
        let tmp = viewData.items[index + 1]
        viewData.items[index + 1] = viewData.items[index]
        viewData.items[index] = tmp
    }
}

#if DEBUG
struct StepQuizSortingView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizSortingView(viewData: StepQuizSortingViewData.makePlaceholder())
            .padding()
    }
}
#endif
