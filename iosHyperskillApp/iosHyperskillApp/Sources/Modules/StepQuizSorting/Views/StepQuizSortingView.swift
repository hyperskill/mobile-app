import SwiftUI

struct StepQuizSortingView: View {
    @State var viewData: StepQuizSortingViewData

    var body: some View {
        VStack(alignment: .leading, spacing: 20) {
            ForEach(Array(viewData.items.enumerated()), id: \.element) { index, item in
                StepQuizSortingItemView(
                    text: item.text,
                    position: index,
                    maxPosition: viewData.items.count - 1,
                    moveUp: {
                        withAnimation {
                            moveUp(position: index)
                        }
                    },
                    moveDown: {
                        withAnimation {
                            moveDown(position: index)
                        }
                    }
                )
            }
        }
    }

    private func moveUp(position: Int) {
        guard position > 0 else {
            return
        }
        let tmp = viewData.items[position - 1]
        viewData.items[position - 1] = viewData.items[position]
        viewData.items[position] = tmp
    }

    private func moveDown(position: Int) {
        guard position < viewData.items.count - 1 else {
            return
        }
        let tmp = viewData.items[position + 1]
        viewData.items[position + 1] = viewData.items[position]
        viewData.items[position] = tmp
    }
}

#if DEBUG
struct StepQuizSortingView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizSortingView(viewData: StepQuizSortingViewData.makePlaceholder())
    }
}
#endif
